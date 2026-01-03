package com.example.womenchildsafetyapp;

public class EmergencyContact {
    private String name;
    private String number;

    public EmergencyContact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}