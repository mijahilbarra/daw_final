package com.cibertec.logistics.controller;

import com.cibertec.logistics.entity.Shipment;
import com.cibertec.logistics.service.ShipmentService;
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
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Shipment> findAll(@RequestParam Map<String, String> filters) {
        return service.findAll(filters);
    }

    @PostMapping
    public Shipment create(@RequestBody Shipment row) {
        return service.create(row);
    }

    @PatchMapping("/{id}")
    public Shipment update(@PathVariable String id, @RequestBody Map<String, Object> patch) {
        return service.update(id, patch);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
