package com.cibertec.logistics.repository;

import com.cibertec.logistics.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
