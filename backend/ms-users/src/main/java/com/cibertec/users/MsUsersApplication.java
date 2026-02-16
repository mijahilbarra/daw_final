package com.cibertec.users;

import com.cibertec.users.entity.UserEntity;
import com.cibertec.users.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUsersApplication.class, args);
    }

    @Bean
    CommandLineRunner seedUsers(UserRepository repository) {
        return args -> {
            if (repository.findByUserEmail("admin@daw.com").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setUserName("Admin DAW");
                admin.setUserEmail("admin@daw.com");
                admin.setUserPassword("admin123");
                admin.setUserRole("admin");
                repository.save(admin);
            }

            if (repository.findByUserEmail("transportista@daw.com").isEmpty()) {
                UserEntity transportista = new UserEntity();
                transportista.setUserName("Transportista Demo");
                transportista.setUserEmail("transportista@daw.com");
                transportista.setUserPassword("trans123");
                transportista.setUserRole("transportista");
                repository.save(transportista);
            }
        };
    }
}
