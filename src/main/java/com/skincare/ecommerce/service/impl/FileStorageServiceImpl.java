package com.skincare.ecommerce.service.impl;

import com.skincare.ecommerce.exception.BadRequestException;
import com.skincare.ecommerce.exception.FileStorageException;
import com.skincare.ecommerce.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("File upload directory initialized at: {}", this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create directory for uploaded files at " + this.fileStorageLocation, ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded file cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum allowed limit of 5MB");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
        String fileExtension = getFileExtension(originalFilename).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new BadRequestException("Invalid file extension: '." + fileExtension + "'. Only jpg, jpeg, png, and webp are allowed");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Invalid content type: '" + contentType + "'. Only jpg, jpeg, png, and webp images are allowed");
        }

        // Generate unique filename: uuid-filename or uuid.ext
        String uniqueFilename = UUID.randomUUID() + "." + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename).normalize();

            // Guard against path traversal
            if (!targetLocation.startsWith(this.fileStorageLocation)) {
                throw new BadRequestException("Invalid file path sequence in filename: " + originalFilename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            log.info("Successfully stored file {} as {}", originalFilename, uniqueFilename);
            return "/uploads/" + uniqueFilename;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file " + uniqueFilename, ex);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            return;
        }

        try {
            String filename = fileUrl.replace("/uploads/", "");
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            if (filePath.startsWith(this.fileStorageLocation) && Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Deleted old file: {}", filePath);
            }
        } catch (Exception ex) {
            log.warn("Could not delete file at {}: {}", fileUrl, ex.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}
