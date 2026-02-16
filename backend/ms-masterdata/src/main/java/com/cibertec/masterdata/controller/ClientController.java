package com.cibertec.masterdata.controller;

import com.cibertec.masterdata.entity.Client;
import com.cibertec.masterdata.repository.ClientRepository;
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
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository repository;
    private final CrudFilterService filterService;

    public ClientController(ClientRepository repository, CrudFilterService filterService) {
        this.repository = repository;
        this.filterService = filterService;
    }

    @GetMapping
    public List<Client> findAll(@RequestParam Map<String, String> filters) {
        return filterService.filter(repository.findAll(), filters, Client.class);
    }

    @PostMapping
    public Client create(@RequestBody Client row) {
        return repository.save(row);
    }

    @PatchMapping("/{id}")
    public Client update(@PathVariable String id, @RequestBody Map<String, Object> patch) {
        Client row = repository.findById(id).orElseThrow();
        if (patch.containsKey("companyCode")) row.setCompanyCode(String.valueOf(patch.get("companyCode")));
        if (patch.containsKey("companyName")) row.setCompanyName(String.valueOf(patch.get("companyName")));
        if (patch.containsKey("address")) row.setAddress(String.valueOf(patch.get("address")));
        if (patch.containsKey("contactName")) row.setContactName(String.valueOf(patch.get("contactName")));
        if (patch.containsKey("email")) row.setEmail(String.valueOf(patch.get("email")));
        if (patch.containsKey("phone")) row.setPhone(String.valueOf(patch.get("phone")));
        return repository.save(row);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
