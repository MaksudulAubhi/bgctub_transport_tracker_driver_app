package com.example.bgctub_transport_tracker_driver_app.model;

public class TransportInformation {
    private String user_id;


    private String driver_name;
    private String driver_contact;
    private String driver_address;

    private String start_time_schedule;
    private String start_date_schedule;
    private String start_location;
    private String travel_road;
    private String destinition;

    private String vehicle_name;
    private String vehicle_number;


    public TransportInformation(String user_id, String driver_name, String driver_contact, String driver_address, String start_time_schedule, String start_date_schedule, String start_location, String destinition, String vehicle_name, String vehicle_number, String travel_road) {
        this.user_id = user_id;
        this.driver_name = driver_name;
        this.driver_contact = driver_contact;
        this.driver_address = driver_address;
        this.start_time_schedule = start_time_schedule;
        this.start_date_schedule = start_date_schedule;
        this.start_location = start_location;
        this.destinition = destinition;
        this.vehicle_name = vehicle_name;
        this.vehicle_number = vehicle_number;
        this.travel_road = travel_road;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_contact() {
        return driver_contact;
    }

    public void setDriver_contact(String driver_contact) {
        this.driver_contact = driver_contact;
    }

    public String getDriver_address() {
        return driver_address;
    }

    public void setDriver_address(String driver_address) {
        this.driver_address = driver_address;
    }

    public String getStart_time_schedule() {
        return start_time_schedule;
    }

    public void setStart_time_schedule(String start_time_schedule) {
        this.start_time_schedule = start_time_schedule;
    }

    public String getstart_date_schedule() {
        return start_date_schedule;
    }

    public void setstart_date_schedule(String start_date_schedule) {
        this.start_date_schedule = start_date_schedule;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getDestinition() {
        return destinition;
    }

    public void setDestinition(String destinition) {
        this.destinition = destinition;
    }


    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getTravel_road() {
        return travel_road;
    }

    public void setTravel_road(String travel_road) {
        this.travel_road = travel_road;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
