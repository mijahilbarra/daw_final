package com.cibertec.masterdata.repository;

import com.cibertec.masterdata.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
