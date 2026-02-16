package com.cibertec.masterdata.controller;

import com.cibertec.masterdata.entity.Category;
import com.cibertec.masterdata.repository.CategoryRepository;
import com.cibertec.masterdata.service.CrudFilterService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository repository;
    private final CrudFilterService filterService;

    public CategoryController(CategoryRepository repository, CrudFilterService filterService) {
        this.repository = repository;
        this.filterService = filterService;
    }

    @GetMapping
    public List<Category> findAll(@RequestParam Map<String, String> filters) {
        return filterService.filter(repository.findAll(), filters, Category.class);
    }

    @PostMapping
    public Category create(@RequestBody Category row) {
        return repository.save(row);
    }

    @PatchMapping("/{id}")
    public Category update(@PathVariable String id, @RequestBody Map<String, Object> patch) {
        Category row = repository.findById(id).orElseThrow();
        if (patch.containsKey("categoryName")) row.setCategoryName(String.valueOf(patch.get("categoryName")));
        return repository.save(row);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
