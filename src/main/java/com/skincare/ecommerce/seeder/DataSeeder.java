package com.skincare.ecommerce.seeder;

import com.skincare.ecommerce.entity.Category;
import com.skincare.ecommerce.entity.Product;
import com.skincare.ecommerce.entity.SkinType;
import com.skincare.ecommerce.repository.CategoryRepository;
import com.skincare.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            log.info("Database already seeded. Skipping sample data population.");
            return;
        }

        log.info("Seeding database with skincare categories and sample products...");

        // 1. Seed 5 Categories
        Category cleanser = Category.builder()
                .name("Cleanser")
                .description("Purifying and gentle facial cleansers for removing dirt, makeup, and excess sebum")
                .build();

        Category toner = Category.builder()
                .name("Toner")
                .description("Balancing and hydrating toners to prepare and strengthen the skin barrier")
                .build();

        Category serum = Category.builder()
                .name("Serum")
                .description("High-potency concentrated active treatments targeting specific skin concerns")
                .build();

        Category moisturizer = Category.builder()
                .name("Moisturizer")
                .description("Hydrating creams, gels, and lotions that lock in moisture and nourish the skin")
                .build();

        Category sunscreen = Category.builder()
                .name("Sunscreen")
                .description("Broad spectrum UV protection against harmful UVA and UVB rays")
                .build();

        List<Category> savedCategories = categoryRepository.saveAll(
                Arrays.asList(cleanser, toner, serum, moisturizer, sunscreen)
        );

        Category savedCleanser = savedCategories.get(0);
        Category savedToner = savedCategories.get(1);
        Category savedSerum = savedCategories.get(2);
        Category savedMoisturizer = savedCategories.get(3);
        Category savedSunscreen = savedCategories.get(4);

        // 2. Seed 10 Sample Skincare Products (without real images, imageUrl = null)
        List<Product> products = List.of(
                Product.builder()
                        .name("CeraVe Hydrating Facial Cleanser")
                        .brand("CeraVe")
                        .description("Non-foaming cleanser that removes dirt, oil, and makeup while keeping skin hydrated with 3 essential ceramides.")
                        .price(new BigDecimal("14.99"))
                        .stock(45)
                        .skinType(SkinType.NORMAL)
                        .ingredients("Aqua / Water, Glycerin, Cetearyl Alcohol, Ceramide NP, Ceramide AP, Ceramide EOP, Hyaluronic Acid")
                        .volume("237ml")
                        .imageUrl(null)
                        .isBestSeller(true)
                        .isNew(false)
                        .category(savedCleanser)
                        .build(),

                Product.builder()
                        .name("La Roche-Posay Effaclar Purifying Foaming Gel")
                        .brand("La Roche-Posay")
                        .description("Gentle foaming gel cleanser designed for oily, acne-prone, and sensitive skin.")
                        .price(new BigDecimal("19.99"))
                        .stock(30)
                        .skinType(SkinType.OILY)
                        .ingredients("Aqua, Sodium Laureth Sulfate, PEG-8, Coco-Betaine, Hexylene Glycol, Zinc PCA")
                        .volume("200ml")
                        .imageUrl(null)
                        .isBestSeller(true)
                        .isNew(false)
                        .category(savedCleanser)
                        .build(),

                Product.builder()
                        .name("Paula's Choice 2% BHA Liquid Exfoliant")
                        .brand("Paula's Choice")
                        .description("Leave-on exfoliant with salicylic acid that clears clogged pores, reduces fine lines, and smooths skin.")
                        .price(new BigDecimal("34.00"))
                        .stock(60)
                        .skinType(SkinType.COMBINATION)
                        .ingredients("Water, Methylpropanediol, Butylene Glycol, Salicylic Acid, Polysorbate 20, Camellia Oleifera Leaf Extract")
                        .volume("118ml")
                        .imageUrl(null)
                        .isBestSeller(true)
                        .isNew(false)
                        .category(savedToner)
                        .build(),

                Product.builder()
                        .name("Anua Heartleaf 77% Soothing Toner")
                        .brand("Anua")
                        .description("Mild, soothing toner packed with 77% heartleaf extract to calm redness and troubled skin.")
                        .price(new BigDecimal("23.00"))
                        .stock(50)
                        .skinType(SkinType.SENSITIVE)
                        .ingredients("Houttuynia Cordata Extract (77%), Water, 1,2-Hexanediol, Glycerin, Betaine, Panthenol")
                        .volume("250ml")
                        .imageUrl(null)
                        .isBestSeller(false)
                        .isNew(true)
                        .category(savedToner)
                        .build(),

                Product.builder()
                        .name("The Ordinary Niacinamide 10% + Zinc 1%")
                        .brand("The Ordinary")
                        .description("High-strength vitamin and mineral blemish formula that visibly reduces blemishes and balances sebum.")
                        .price(new BigDecimal("6.50"))
                        .stock(120)
                        .skinType(SkinType.COMBINATION)
                        .ingredients("Aqua, Niacinamide, Pentylene Glycol, Zinc PCA, Dimethyl Isosorbide, Tamarindus Indica Seed Gum")
                        .volume("30ml")
                        .imageUrl(null)
                        .isBestSeller(true)
                        .isNew(false)
                        .category(savedSerum)
                        .build(),

                Product.builder()
                        .name("SkinCeuticals C E Ferulic Serum")
                        .brand("SkinCeuticals")
                        .description("Patented daytime vitamin C serum that delivers advanced environmental protection and improves signs of aging.")
                        .price(new BigDecimal("182.00"))
                        .stock(15)
                        .skinType(SkinType.ALL)
                        .ingredients("Aqua, Ethoxydiglycol, L-Ascorbic Acid, Propylene Glycol, Glycerin, Laureth-23, Alpha Tocopherol, Ferulic Acid")
                        .volume("30ml")
                        .imageUrl(null)
                        .isBestSeller(false)
                        .isNew(false)
                        .category(savedSerum)
                        .build(),

                Product.builder()
                        .name("COSRX Advanced Snail 96 Mucin Power Essence")
                        .brand("COSRX")
                        .description("Lightweight essence formulated with 96% snail secretion filtrate to deeply hydrate and repair skin damage.")
                        .price(new BigDecimal("25.00"))
                        .stock(80)
                        .skinType(SkinType.ALL)
                        .ingredients("Snail Secretion Filtrate, Betaine, Butylene Glycol, 1,2-Hexanediol, Sodium Hyaluronate, Panthenol, Arginine")
                        .volume("100ml")
                        .imageUrl(null)
                        .isBestSeller(true)
                        .isNew(false)
                        .category(savedSerum)
                        .build(),

                Product.builder()
                        .name("Kiehl's Ultra Facial Cream")
                        .brand("Kiehl's")
                        .description("24-hour daily facial moisturizer with squalane that provides continuous barrier hydration.")
                        .price(new BigDecimal("38.00"))
                        .stock(40)
                        .skinType(SkinType.DRY)
                        .ingredients("Aqua, Glycerin, Cyclohexasiloxane, Squalane, Bis-PEG-18 Methyl Ether Dimethyl Silane, Sucrose Stearate")
                        .volume("50ml")
                        .imageUrl(null)
                        .isBestSeller(true)
                        .isNew(false)
                        .category(savedMoisturizer)
                        .build(),

                Product.builder()
                        .name("Beauty of Joseon Relief Sun: Rice + Probiotics SPF50+")
                        .brand("Beauty of Joseon")
                        .description("Organic chemical sunscreen with rice extract and grain-fermented extracts that leaves no white cast.")
                        .price(new BigDecimal("18.00"))
                        .stock(95)
                        .skinType(SkinType.SENSITIVE)
                        .ingredients("Water, Oryza Sativa (Rice) Extract (30%), Dibutyl Adipate, Propanediol, Diethylamino Hydroxybenzoyl Hexyl Benzoate")
                        .volume("50ml")
                        .imageUrl(null)
                        .isBestSeller(true)
                        .isNew(true)
                        .category(savedSunscreen)
                        .build(),

                Product.builder()
                        .name("La Roche-Posay Anthelios UVMune 400 Invisible Fluid SPF50+")
                        .brand("La Roche-Posay")
                        .description("Ultra-high broad-spectrum protection against ultra-long UVA rays with invisible, non-greasy finish.")
                        .price(new BigDecimal("22.50"))
                        .stock(65)
                        .skinType(SkinType.NORMAL)
                        .ingredients("Aqua, Alcohol Denat., Triethyl Citrate, Diisopropyl Sebacate, Silica, Ethylhexyl Salicylate")
                        .volume("50ml")
                        .imageUrl(null)
                        .isBestSeller(false)
                        .isNew(true)
                        .category(savedSunscreen)
                        .build()
        );

        productRepository.saveAll(products);
        log.info("Successfully seeded 5 categories and 10 skincare products!");
    }
}
