package com.duva.duva.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class DeliveryLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    private double loadSize;
    private LocalTime startWindow;
    private LocalTime endWindow;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLoadSize() {
        return loadSize;
    }

    public void setLoadSize(double loadSize) {
        this.loadSize = loadSize;
    }

    public LocalTime getStartWindow() {
        return startWindow;
    }

    public void setStartWindow(LocalTime startWindow) {
        this.startWindow = startWindow;
    }

    public LocalTime getEndWindow() {
        return endWindow;
    }

    public void setEndWindow(LocalTime endWindow) {
        this.endWindow = endWindow;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
