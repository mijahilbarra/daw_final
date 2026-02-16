package com.cibertec.logistics.service;

import com.cibertec.logistics.client.MasterdataClient;
import com.cibertec.logistics.entity.Shipment;
import com.cibertec.logistics.messaging.ShipmentEventPublisher;
import com.cibertec.logistics.repository.ShipmentRepository;
import com.cibertec.logistics.repository.TransportRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final TransportRepository transportRepository;
    private final FilterService filterService;
    private final MasterdataClient masterdataClient;
    private final ShipmentEventPublisher eventPublisher;

    public ShipmentService(ShipmentRepository repository,
                           TransportRepository transportRepository,
                           FilterService filterService,
                           MasterdataClient masterdataClient,
                           ShipmentEventPublisher eventPublisher) {
        this.repository = repository;
        this.transportRepository = transportRepository;
        this.filterService = filterService;
        this.masterdataClient = masterdataClient;
        this.eventPublisher = eventPublisher;
    }

    public List<Shipment> findAll(Map<String, String> filters) {
        return filterService.filter(repository.findAll(), filters, Shipment.class);
    }

    public Shipment create(Shipment row) {
        validateReferences(row.getShipmentClientId(), row.getShipmentCategoryId(), row.getShipmentStatusId(), row.getShipmentTransportId());
        Shipment saved = repository.save(row);
        eventPublisher.publishCreated(saved);
        return saved;
    }

    public Shipment update(String id, Map<String, Object> patch) {
        Shipment row = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Envio no encontrado"));

        String previousStatus = row.getShipmentStatusId();

        if (patch.containsKey("shipmentCategoryId")) row.setShipmentCategoryId(stringOrNull(patch.get("shipmentCategoryId")));
        if (patch.containsKey("shipmentDescription")) row.setShipmentDescription(stringOrNull(patch.get("shipmentDescription")));
        if (patch.containsKey("shipmentPrice")) row.setShipmentPrice(doubleOrNull(patch.get("shipmentPrice")));
        if (patch.containsKey("shipmentWeight")) row.setShipmentWeight(doubleOrNull(patch.get("shipmentWeight")));
        if (patch.containsKey("shipmentVolume")) row.setShipmentVolume(doubleOrNull(patch.get("shipmentVolume")));
        if (patch.containsKey("shipmentOrigin")) row.setShipmentOrigin(stringOrNull(patch.get("shipmentOrigin")));
        if (patch.containsKey("shipmentDestination")) row.setShipmentDestination(stringOrNull(patch.get("shipmentDestination")));
        if (patch.containsKey("shipmentStatusId")) row.setShipmentStatusId(stringOrNull(patch.get("shipmentStatusId")));
        if (patch.containsKey("shipmentDate")) row.setShipmentDate(LocalDate.parse(String.valueOf(patch.get("shipmentDate"))));
        if (patch.containsKey("shipmentClientId")) row.setShipmentClientId(stringOrNull(patch.get("shipmentClientId")));
        if (patch.containsKey("shipmentTransportId")) row.setShipmentTransportId(stringOrNull(patch.get("shipmentTransportId")));

        validateReferences(row.getShipmentClientId(), row.getShipmentCategoryId(), row.getShipmentStatusId(), row.getShipmentTransportId());

        Shipment saved = repository.save(row);

        if (saved.getShipmentStatusId() != null && !saved.getShipmentStatusId().equals(previousStatus)) {
            eventPublisher.publishStatusChanged(saved);
        }

        return saved;
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private void validateReferences(String clientId, String categoryId, String statusId, String transportId) {
        if (clientId != null && !clientId.isBlank() && masterdataClient.findClients(clientId).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Cliente no encontrado");
        }
        if (categoryId != null && !categoryId.isBlank() && masterdataClient.findCategories(categoryId).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Categoria no encontrada");
        }
        if (statusId != null && !statusId.isBlank() && masterdataClient.findStatuses(statusId).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Estado no encontrado");
        }
        if (transportId != null && !transportId.isBlank() && transportRepository.findById(transportId).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Transporte no encontrado");
        }
    }

    private String stringOrNull(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private Double doubleOrNull(Object value) {
        if (value == null) return null;
        return Double.parseDouble(String.valueOf(value));
    }
}
