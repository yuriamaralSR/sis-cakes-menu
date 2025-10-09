package com.asrcore.sis_cakes_menu.infra.config;

import com.asrcore.sis_cakes_menu.model.User;
import com.asrcore.sis_cakes_menu.model.enums.UserRole;
import com.asrcore.sis_cakes_menu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Profile("dev")
public class DevConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String encryptedPassword = new BCryptPasswordEncoder().encode("admin");
        User admin = new User("admin", "user admin", "000000000", encryptedPassword,  UserRole.ADMIN);

        userRepository.save(admin);
    }
}
