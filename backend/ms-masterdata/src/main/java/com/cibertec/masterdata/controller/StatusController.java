package com.cibertec.masterdata.controller;

import com.cibertec.masterdata.entity.ShipmentStatus;
import com.cibertec.masterdata.repository.ShipmentStatusRepository;
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
@RequestMapping("/statuses")
public class StatusController {

    private final ShipmentStatusRepository repository;
    private final CrudFilterService filterService;

    public StatusController(ShipmentStatusRepository repository, CrudFilterService filterService) {
        this.repository = repository;
        this.filterService = filterService;
    }

    @GetMapping
    public List<ShipmentStatus> findAll(@RequestParam Map<String, String> filters) {
        return filterService.filter(repository.findAll(), filters, ShipmentStatus.class);
    }

    @PostMapping
    public ShipmentStatus create(@RequestBody ShipmentStatus row) {
        return repository.save(row);
    }

    @PatchMapping("/{id}")
    public ShipmentStatus update(@PathVariable String id, @RequestBody Map<String, Object> patch) {
        ShipmentStatus row = repository.findById(id).orElseThrow();
        if (patch.containsKey("statusName")) row.setStatusName(String.valueOf(patch.get("statusName")));
        return repository.save(row);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
