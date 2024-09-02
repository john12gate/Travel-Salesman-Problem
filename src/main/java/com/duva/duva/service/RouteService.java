package com.duva.duva.service;

import com.duva.duva.entity.Route;

import java.util.List;

public interface RouteService {

    Route optimizeRoute(List<Long> locationIds, Long depotId, Long vehicleId);

    // Add the method signature for getting a Route by ID
    Route getRouteById(Long routeId);

}

