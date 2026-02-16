package com.cibertec.logistics.repository;

import com.cibertec.logistics.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRepository extends JpaRepository<Transport, String> {
}
