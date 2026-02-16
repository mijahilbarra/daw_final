package com.cibertec.masterdata.repository;

import com.cibertec.masterdata.entity.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, String> {
}
