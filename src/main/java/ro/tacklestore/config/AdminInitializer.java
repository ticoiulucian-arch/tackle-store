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
            // Save root categories first
            var rods = cat("Rods", "Fishing rods for all techniques.", List.of("Length (m)", "Casting Weight (g)", "Sections", "Weight (g)", "Material", "Action"));
            var reels = cat("Reels", "Spinning, baitcasting and big pit reels.", List.of("Gear Ratio", "Spool Capacity (mm/m)", "Bearings", "Weight (g)", "Size", "Drag Type"));
            var lines = cat("Lines & Leaders", "Monofilament, fluorocarbon and braided lines.", List.of("Type", "Diameter (mm)", "Breaking Strain (kg)", "Length (m)", "Colour", "Stretch"));
            var hooks = cat("Hooks", "Single, treble and specialty hooks.", List.of("Hook Size", "Type", "Material", "Barbed/Barbless", "Pieces per Pack"));
            var feeders = cat("Feeders & Weights", "Method feeders, cage feeders, leads and sinkers.", List.of("Weight (g)", "Type", "Material", "Size", "Mould Included"));
            var baits = cat("Baits & Groundbait", "Boilies, pellets, groundbait, pop-ups and wafters.", List.of("Type", "Weight (kg)", "Flavour", "Size (mm)", "Solubility"));
            var rigs = cat("Rigs & Terminal Tackle", "Ready rigs, swivels, clips and end tackle.", List.of("Hook Size", "Hooklink Length (cm)", "Diameter (mm)", "Material", "Type", "Pieces per Pack"));
            var rodPods = cat("Rod Pods & Bite Alarms", "Rod pods, bank sticks, buzz bars and bite alarms.", List.of("Type", "Material", "Weight (g)", "Adjustable Length", "Compatibility"));
            var nets = cat("Landing Nets & Keepnets", "Landing nets, keepnets, handles and accessories.", List.of("Net Size (cm)", "Handle Length (m)", "Mesh Size", "Material", "Foldable"));
            var luggage = cat("Luggage & Storage", "Carryalls, rod holdalls, tackle boxes and bags.", List.of("Type", "Dimensions (cm)", "Material", "Compartments", "Waterproof"));
            var chairs = cat("Chairs & Bedchairs", "Fishing chairs, seatboxes, bedchairs and shelters.", List.of("Type", "Weight Capacity (kg)", "Weight (g)", "Foldable", "Material"));
            var clothing = cat("Clothing & Footwear", "Jackets, trousers, boots, waders and thermal wear.", List.of("Type", "Size", "Material", "Waterproof", "Breathable"));
            var electronics = cat("Electronics", "Fish finders, headlamps, power banks and electronic gear.", List.of("Type", "Battery", "Weight (g)", "Waterproof Rating", "Features"));
            var accessories = cat("Accessories", "Tools, scales, slings, mats and general accessories.", List.of("Accessory Type", "Size", "Material", "Pieces per Pack", "Compatibility"));

            categoryRepository.saveAll(List.of(rods, reels, lines, hooks, feeders, baits, rigs, rodPods, nets, luggage, chairs, clothing, electronics, accessories));

            // Subcategories
            categoryRepository.saveAll(List.of(
                sub("Feeder Rods", rods), sub("Match Rods", rods), sub("Bolognese Rods", rods),
                sub("Spinning Rods", rods), sub("Carp Rods", rods), sub("Float Rods", rods), sub("Telescopic Rods", rods),

                sub("Spinning Reels", reels), sub("Baitcasting Reels", reels), sub("Big Pit Reels", reels),
                sub("Feeder Reels", reels), sub("Match Reels", reels),

                sub("Monofilament", lines), sub("Fluorocarbon", lines), sub("Braided Line", lines),
                sub("Leaders", lines), sub("Shock Leaders", lines),

                sub("Single Hooks", hooks), sub("Treble Hooks", hooks), sub("Barbless Hooks", hooks),
                sub("Carp Hooks", hooks), sub("Feeder Hooks", hooks),

                sub("Method Feeders", feeders), sub("Cage Feeders", feeders), sub("Flat Feeders", feeders),
                sub("Leads & Sinkers", feeders), sub("Feeder Moulds", feeders),

                sub("Boilies", baits), sub("Pellets", baits), sub("Groundbait Mixes", baits),
                sub("Pop-ups & Wafters", baits), sub("Liquid Additives", baits), sub("Dips & Glugs", baits),
                sub("Corn & Particles", baits),

                sub("Ready Rigs", rigs), sub("Swivels & Clips", rigs), sub("Beads & Sleeves", rigs),
                sub("Lead Clips", rigs), sub("Hair Rigs", rigs), sub("Rig Tubing", rigs),

                sub("Rod Pods", rodPods), sub("Bank Sticks", rodPods), sub("Buzz Bars", rodPods),
                sub("Electronic Bite Alarms", rodPods), sub("Bite Indicators", rodPods),

                sub("Landing Nets", nets), sub("Keepnets", nets), sub("Net Handles", nets),
                sub("Net Accessories", nets),

                sub("Carryalls", luggage), sub("Rod Holdalls", luggage), sub("Tackle Boxes", luggage),
                sub("Rucksacks", luggage), sub("Cool Bags", luggage),

                sub("Fishing Chairs", chairs), sub("Seatboxes", chairs), sub("Bedchairs", chairs),
                sub("Shelters & Bivvies", chairs),

                sub("Jackets", clothing), sub("Trousers", clothing), sub("Boots", clothing),
                sub("Waders", clothing), sub("Thermal Underwear", clothing), sub("Hats & Gloves", clothing),

                sub("Fish Finders", electronics), sub("Headlamps", electronics), sub("Power Banks", electronics),
                sub("Bite Alarm Sets", electronics), sub("Electronic Scales", electronics),

                sub("Tools & Pliers", accessories), sub("Disgorgers", accessories), sub("Weighing Slings", accessories),
                sub("Unhooking Mats", accessories), sub("Rod Bands & Straps", accessories), sub("Bait Accessories", accessories)
            ));

            log.info("Seeded 14 root categories with ~70 subcategories");
        }
    }

    private Category cat(String name, String description, List<String> specTemplate) {
        return Category.builder().name(name).description(description).specTemplate(specTemplate).build();
    }

    private Category sub(String name, Category parent) {
        return Category.builder().name(name).parent(parent).specTemplate(parent.getSpecTemplate()).build();
    }
}
