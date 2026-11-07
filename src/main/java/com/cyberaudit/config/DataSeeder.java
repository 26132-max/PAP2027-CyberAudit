package com.cyberaudit.config;

import com.cyberaudit.domain.Organization;
import com.cyberaudit.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedOrganizations(OrganizationRepository organizationRepository) {
        return args -> {
            if (organizationRepository.count() == 0) {
                organizationRepository.save(new Organization("AlphaTech", "Tecnologia", "Ativa"));
                organizationRepository.save(new Organization("Nexa Systems", "Consultoria IT", "Ativa"));
                organizationRepository.save(new Organization("BlueCore Labs", "Desenvolvimento de Software", "Em revisão"));
            }
        };
    }
}
