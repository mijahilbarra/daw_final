package com.cibertec.logistics.controller;

import com.cibertec.logistics.entity.Transport;
import com.cibertec.logistics.service.TransportService;
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
@RequestMapping("/transports")
public class TransportController {

    private final TransportService service;

    public TransportController(TransportService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transport> findAll(@RequestParam Map<String, String> filters) {
        return service.findAll(filters);
    }

    @PostMapping
    public Transport create(@RequestBody Transport row) {
        return service.create(row);
    }

    @PatchMapping("/{id}")
    public Transport update(@PathVariable String id, @RequestBody Map<String, Object> patch) {
        return service.update(id, patch);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
