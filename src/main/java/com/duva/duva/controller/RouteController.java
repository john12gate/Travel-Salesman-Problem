package com.duva.duva.controller;

import com.duva.duva.entity.Route;
import com.duva.duva.service.RouteService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/optimize")
    public ResponseEntity<Route> optimizeRoute(@RequestBody RouteRequest routeRequest) {
        Route optimizedRoute = routeService.optimizeRoute(routeRequest.getLocationIds(), routeRequest.getDepotId(), routeRequest.getVehicleId());
        return ResponseEntity.ok(optimizedRoute);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable Long id) {
        Route route = routeService.getRouteById(id);
        return ResponseEntity.ok(route);
    }
}

