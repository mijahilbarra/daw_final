package com.cibertec.masterdata.repository;

import com.cibertec.masterdata.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}
