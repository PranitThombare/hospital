package com.example.hospital.config;

import com.example.hospital.entity.Role;
import com.example.hospital.entity.User;
import com.example.hospital.repository.RoleRepository;
import com.example.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1️⃣ CREATE ROLES (IDEMPOTENT)
        List<String> roleNames = List.of("PATIENT", "DOCTOR", "ADMIN");

        for (String roleName : roleNames) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(roleName);
                        System.out.println("Created role: " + roleName);
                        return roleRepository.save(role);
                    });
        }

        // 2️⃣ CREATE ADMIN USER (ONLY IF NOT EXISTS)
        String adminEmail = "admin@hospital.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123")); // 🔐 encrypted
            admin.setEnabled(true);
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

            System.out.println("✅ Admin user created: " + adminEmail);
        } else {
            System.out.println("ℹ️ Admin user already exists");
        }

        System.out.println("✅ Data initialization completed");
    }
}
