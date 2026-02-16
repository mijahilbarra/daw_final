package com.cibertec.logistics;

import com.cibertec.logistics.entity.Transport;
import com.cibertec.logistics.repository.TransportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class MsLogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsLogisticsApplication.class, args);
    }

    @Bean
    CommandLineRunner seedTransport(TransportRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Transport row = new Transport();
                row.setTransportType("Camion");
                row.setTransportCapacity(1000D);
                row.setTransportStatus("available");
                row.setTransportLocation("Lima");
                row.setTransportDriver("Chofer Demo");
                row.setTransportLicensePlate("ABC-123");
                row.setTransportCompany("LogiDemo");
                repository.save(row);
            }
        };
    }
}
