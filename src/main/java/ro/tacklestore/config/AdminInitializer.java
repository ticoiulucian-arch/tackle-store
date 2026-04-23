package ro.tacklestore.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.tacklestore.model.AdminUser;
import ro.tacklestore.model.Category;
import ro.tacklestore.repository.AdminUserRepository;
import ro.tacklestore.repository.CategoryRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final AdminUserRepository adminUserRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (adminUserRepository.findByUsername("admin").isEmpty()) {
            adminUserRepository.save(AdminUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .build());
            log.info("Admin user created: admin / admin123");
        }

        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                cat("Feeder Rods", "Rods specially designed for method feeder fishing, with interchangeable sensitive tips.",
                    List.of("Length (m)", "Casting Weight (g)", "Sections", "Weight (g)", "Material", "Tips Included")),
                cat("Feeder Reels", "Reels with fast retrieve and precise drag, ideal for method feeder fishing.",
                    List.of("Gear Ratio", "Spool Capacity (mm/m)", "Bearings", "Weight (g)", "Size", "Drag Type")),
                cat("Method Feeders", "Method feeders of various shapes and weights, along with moulds for compacting groundbait.",
                    List.of("Weight (g)", "Type (flat/banjo/cage)", "Material", "Size", "Mould Included")),
                cat("Baits & Groundbait", "Specialist method feeder groundbaits, pellets, boilies, pop-ups and wafters.",
                    List.of("Type (groundbait/pellet/boilies/wafters)", "Weight (kg)", "Flavour", "Size (mm)", "Solubility")),
                cat("Rigs & Hooks", "Ready-tied method feeder rigs and high-strength hooks.",
                    List.of("Hook Size", "Hooklink Length (cm)", "Hooklink Diameter (mm)", "Hook Material", "Type (tied/untied)", "Pieces per Pack")),
                cat("Lines & Leaders", "Monofilament, fluorocarbon and braided lines for method feeder fishing.",
                    List.of("Type (mono/fluoro/braid)", "Diameter (mm)", "Breaking Strain (kg)", "Length (m)", "Colour", "Stretch")),
                cat("Accessories", "Clips, swivels, pellet bands, bait bands, baiting needles and other tools.",
                    List.of("Accessory Type", "Size", "Material", "Pieces per Pack", "Compatibility"))
            ));
            log.info("Seeded 7 method feeder categories with spec templates");
        }
    }

    private Category cat(String name, String description, List<String> specTemplate) {
        return Category.builder().name(name).description(description).specTemplate(specTemplate).build();
    }
}
