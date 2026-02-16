package com.cibertec.logistics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private String shipmentCategoryId;
    private String shipmentDescription;
    private Double shipmentPrice;
    private Double shipmentWeight;
    private Double shipmentVolume;
    private String shipmentOrigin;
    private String shipmentDestination;
    private String shipmentStatusId;
    private LocalDate shipmentDate;
    private String shipmentClientId;
    private String shipmentTransportId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShipmentCategoryId() {
        return shipmentCategoryId;
    }

    public void setShipmentCategoryId(String shipmentCategoryId) {
        this.shipmentCategoryId = shipmentCategoryId;
    }

    public String getShipmentDescription() {
        return shipmentDescription;
    }

    public void setShipmentDescription(String shipmentDescription) {
        this.shipmentDescription = shipmentDescription;
    }

    public Double getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(Double shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }

    public Double getShipmentWeight() {
        return shipmentWeight;
    }

    public void setShipmentWeight(Double shipmentWeight) {
        this.shipmentWeight = shipmentWeight;
    }

    public Double getShipmentVolume() {
        return shipmentVolume;
    }

    public void setShipmentVolume(Double shipmentVolume) {
        this.shipmentVolume = shipmentVolume;
    }

    public String getShipmentOrigin() {
        return shipmentOrigin;
    }

    public void setShipmentOrigin(String shipmentOrigin) {
        this.shipmentOrigin = shipmentOrigin;
    }

    public String getShipmentDestination() {
        return shipmentDestination;
    }

    public void setShipmentDestination(String shipmentDestination) {
        this.shipmentDestination = shipmentDestination;
    }

    public String getShipmentStatusId() {
        return shipmentStatusId;
    }

    public void setShipmentStatusId(String shipmentStatusId) {
        this.shipmentStatusId = shipmentStatusId;
    }

    public LocalDate getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDate shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentClientId() {
        return shipmentClientId;
    }

    public void setShipmentClientId(String shipmentClientId) {
        this.shipmentClientId = shipmentClientId;
    }

    public String getShipmentTransportId() {
        return shipmentTransportId;
    }

    public void setShipmentTransportId(String shipmentTransportId) {
        this.shipmentTransportId = shipmentTransportId;
    }
}
