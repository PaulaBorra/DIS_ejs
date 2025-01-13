package com.example.demo;

import java.util.UUID;

public class Vehicles {

    private String make; 
    private String model; 
    private int yearOfManufacture; 
    private String type; 
    private String licensePlateNumber; 
    private String uuid;

    public Vehicles(String make, String model, int yearOfManufacture, String type, String licensePlateNumber) {
        this.make = make;
        this.model = model;
        this.yearOfManufacture = yearOfManufacture;
        this.type = type;
        this.licensePlateNumber = licensePlateNumber;
        this.uuid = UUID.randomUUID().toString();
    }

    public Vehicles() {
    }
    
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public int getYearOfManufacture() {
        return yearOfManufacture;
    }
    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }
    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    } 
}