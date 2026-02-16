package com.cibertec.logistics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "transports")
public class Transport {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private String transportUserId;
    private String transportType;
    private Double transportCapacity;
    private String transportStatus;
    private String transportLocation;
    private String transportDriver;
    private String transportLicensePlate;
    private String transportCompany;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransportUserId() {
        return transportUserId;
    }

    public void setTransportUserId(String transportUserId) {
        this.transportUserId = transportUserId;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public Double getTransportCapacity() {
        return transportCapacity;
    }

    public void setTransportCapacity(Double transportCapacity) {
        this.transportCapacity = transportCapacity;
    }

    public String getTransportStatus() {
        return transportStatus;
    }

    public void setTransportStatus(String transportStatus) {
        this.transportStatus = transportStatus;
    }

    public String getTransportLocation() {
        return transportLocation;
    }

    public void setTransportLocation(String transportLocation) {
        this.transportLocation = transportLocation;
    }

    public String getTransportDriver() {
        return transportDriver;
    }

    public void setTransportDriver(String transportDriver) {
        this.transportDriver = transportDriver;
    }

    public String getTransportLicensePlate() {
        return transportLicensePlate;
    }

    public void setTransportLicensePlate(String transportLicensePlate) {
        this.transportLicensePlate = transportLicensePlate;
    }

    public String getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(String transportCompany) {
        this.transportCompany = transportCompany;
    }
}
