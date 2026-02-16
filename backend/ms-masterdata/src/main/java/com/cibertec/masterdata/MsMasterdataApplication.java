package com.cibertec.masterdata;

import com.cibertec.masterdata.entity.Category;
import com.cibertec.masterdata.entity.Client;
import com.cibertec.masterdata.entity.ShipmentStatus;
import com.cibertec.masterdata.repository.CategoryRepository;
import com.cibertec.masterdata.repository.ClientRepository;
import com.cibertec.masterdata.repository.ShipmentStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsMasterdataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsMasterdataApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(ClientRepository clientRepository,
                               CategoryRepository categoryRepository,
                               ShipmentStatusRepository statusRepository) {
        return args -> {
            if (clientRepository.count() == 0) {
                Client client = new Client();
                client.setCompanyCode("CLI-001");
                client.setCompanyName("Cliente Demo");
                client.setAddress("Av. Principal 123");
                client.setContactName("Juan Perez");
                client.setEmail("cliente@demo.com");
                client.setPhone("999111222");
                clientRepository.save(client);
            }

            if (categoryRepository.count() == 0) {
                Category category = new Category();
                category.setCategoryName("General");
                categoryRepository.save(category);
            }

            if (statusRepository.count() == 0) {
                ShipmentStatus status = new ShipmentStatus();
                status.setStatusName("Pendiente");
                statusRepository.save(status);
            }
        };
    }
}
