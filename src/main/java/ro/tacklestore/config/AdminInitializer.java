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
                cat("Rods", "Fishing rods for all techniques — feeder, match, bolognese, spinning, carp and more.",
                    List.of("Length (m)", "Casting Weight (g)", "Sections", "Weight (g)", "Material", "Action")),
                cat("Reels", "Spinning, baitcasting and big pit reels for all fishing styles.",
                    List.of("Gear Ratio", "Spool Capacity (mm/m)", "Bearings", "Weight (g)", "Size", "Drag Type")),
                cat("Lines & Leaders", "Monofilament, fluorocarbon and braided lines for any situation.",
                    List.of("Type (mono/fluoro/braid)", "Diameter (mm)", "Breaking Strain (kg)", "Length (m)", "Colour", "Stretch")),
                cat("Hooks", "Single, treble and specialty hooks in all sizes.",
                    List.of("Hook Size", "Type", "Material", "Barbed/Barbless", "Pieces per Pack")),
                cat("Feeders & Weights", "Method feeders, cage feeders, leads and sinkers.",
                    List.of("Weight (g)", "Type (flat/banjo/cage/lead)", "Material", "Size", "Mould Included")),
                cat("Baits & Groundbait", "Boilies, pellets, groundbait mixes, pop-ups, wafters and live bait.",
                    List.of("Type (groundbait/pellet/boilies/wafters)", "Weight (kg)", "Flavour", "Size (mm)", "Solubility")),
                cat("Rigs & Terminal Tackle", "Ready-tied rigs, swivels, clips, beads and end tackle components.",
                    List.of("Hook Size", "Hooklink Length (cm)", "Hooklink Diameter (mm)", "Material", "Type", "Pieces per Pack")),
                cat("Rod Pods & Bite Alarms", "Rod pods, bank sticks, buzz bars, electronic bite alarms and indicators.",
                    List.of("Type (rod pod/bank stick/bite alarm)", "Material", "Weight (g)", "Adjustable Length", "Compatibility")),
                cat("Landing Nets & Keepnets", "Landing nets, keepnets, handles and net accessories.",
                    List.of("Net Size (cm)", "Handle Length (m)", "Mesh Size", "Material", "Foldable")),
                cat("Luggage & Storage", "Carryalls, rod holdalls, tackle boxes, rucksacks and cool bags.",
                    List.of("Type", "Dimensions (cm)", "Material", "Compartments", "Waterproof")),
                cat("Chairs & Bedchairs", "Fishing chairs, seatboxes, bedchairs and shelters.",
                    List.of("Type (chair/seatbox/bedchair)", "Weight Capacity (kg)", "Weight (g)", "Foldable", "Material")),
                cat("Clothing & Footwear", "Fishing jackets, trousers, boots, waders and thermal wear.",
                    List.of("Type", "Size", "Material", "Waterproof", "Breathable")),
                cat("Electronics", "Fish finders, headlamps, power banks and other electronic gear.",
                    List.of("Type", "Battery", "Weight (g)", "Waterproof Rating", "Features")),
                cat("Accessories", "Tools, disgorgers, scales, slings, mats and general fishing accessories.",
                    List.of("Accessory Type", "Size", "Material", "Pieces per Pack", "Compatibility"))
            ));
            log.info("Seeded 14 categories with spec templates");
        }
    }

    private Category cat(String name, String description, List<String> specTemplate) {
        return Category.builder().name(name).description(description).specTemplate(specTemplate).build();
    }
}
