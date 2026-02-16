package com.cibertec.logistics.service;

import com.cibertec.logistics.client.UsersClient;
import com.cibertec.logistics.entity.Transport;
import com.cibertec.logistics.repository.TransportRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TransportService {

    private final TransportRepository repository;
    private final FilterService filterService;
    private final UsersClient usersClient;

    public TransportService(TransportRepository repository, FilterService filterService, UsersClient usersClient) {
        this.repository = repository;
        this.filterService = filterService;
        this.usersClient = usersClient;
    }

    public List<Transport> findAll(Map<String, String> filters) {
        return filterService.filter(repository.findAll(), filters, Transport.class);
    }

    public Transport create(Transport row) {
        if (row.getTransportStatus() == null || row.getTransportStatus().isBlank()) {
            row.setTransportStatus("available");
        }
        return repository.save(row);
    }

    public Transport update(String id, Map<String, Object> patch) {
        Transport row = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Transporte no encontrado"));

        if (patch.containsKey("transportUserId")) {
            String userId = stringOrNull(patch.get("transportUserId"));
            if (userId != null && !userId.isBlank() && usersClient.findUsers(userId).isEmpty()) {
                throw new ResponseStatusException(NOT_FOUND, "Usuario no encontrado para asignacion");
            }
            row.setTransportUserId((userId == null || userId.isBlank()) ? null : userId);
        }
        if (patch.containsKey("transportType")) row.setTransportType(stringOrNull(patch.get("transportType")));
        if (patch.containsKey("transportCapacity")) row.setTransportCapacity(doubleOrNull(patch.get("transportCapacity")));
        if (patch.containsKey("transportStatus")) row.setTransportStatus(stringOrNull(patch.get("transportStatus")));
        if (patch.containsKey("transportLocation")) row.setTransportLocation(stringOrNull(patch.get("transportLocation")));
        if (patch.containsKey("transportDriver")) row.setTransportDriver(stringOrNull(patch.get("transportDriver")));
        if (patch.containsKey("transportLicensePlate")) row.setTransportLicensePlate(stringOrNull(patch.get("transportLicensePlate")));
        if (patch.containsKey("transportCompany")) row.setTransportCompany(stringOrNull(patch.get("transportCompany")));

        return repository.save(row);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private String stringOrNull(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private Double doubleOrNull(Object value) {
        if (value == null) return null;
        return Double.parseDouble(String.valueOf(value));
    }
}
