package com.duva.duva.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private Depot depot;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<DeliveryLocation> deliveryLocations;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public List<DeliveryLocation> getDeliveryLocations() {
        return deliveryLocations;
    }

    public void setDeliveryLocations(List<DeliveryLocation> deliveryLocations) {
        this.deliveryLocations = deliveryLocations;
    }
}
