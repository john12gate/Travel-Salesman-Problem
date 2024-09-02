package com.duva.duva.controller;

import lombok.Data;

import java.util.List;

@Data
public class RouteRequest {
    private List<Long> locationIds;
    private Long depotId;
    private Long vehicleId;
}
