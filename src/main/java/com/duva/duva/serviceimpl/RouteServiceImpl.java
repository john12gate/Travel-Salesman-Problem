package com.duva.duva.serviceimpl;

import com.duva.duva.entity.DeliveryLocation;
import com.duva.duva.entity.Depot;
import com.duva.duva.entity.Route;
import com.duva.duva.entity.Vehicle;
import com.duva.duva.repository.DeliveryLocationRepository;
import com.duva.duva.repository.DepotRepository;
import com.duva.duva.repository.RouteRepository;
import com.duva.duva.repository.VehicleRepository;
import com.duva.duva.service.RouteService;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    static {
        System.loadLibrary("jniortools");
    }

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DeliveryLocationRepository deliveryLocationRepository;

    @Autowired
    private DepotRepository depotRepository;

    @Override
    public Route optimizeRoute(List<Long> locationIds, Long depotId, Long vehicleId) {
        // Fetch locations, depot, and vehicle
        List<DeliveryLocation> locations = deliveryLocationRepository.findAllById(locationIds);
        Depot depot = depotRepository.findById(depotId).orElseThrow();
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow();

        // Setup OR-Tools routing model
        RoutingIndexManager manager = new RoutingIndexManager(locations.size() + 1, 1, 0);
        RoutingModel routing = new RoutingModel(manager);

        // Define cost function and constraints
        routing.setArcCostEvaluatorOfAllVehicles(routing.registerTransitCallback((long fromIndex, long toIndex) -> {
            int fromNode = (int) manager.indexToNode(fromIndex);
            int toNode = (int) manager.indexToNode(toIndex);
            return calculateDistance(fromNode, toNode, locations, depot);
        }));

        // Solve the problem
        RoutingSearchParameters searchParameters = RoutingSearchParameters.newBuilder()
                .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                .build();
        Assignment solution = routing.solveWithParameters(searchParameters);

        if (solution != null) {
            // Map the solution back to Route entity and save it in the database
            Route route = new Route();
            route.setVehicle(vehicle);
            route.setDepot(depot);
            route.setDeliveryLocations(mapSolutionToLocations(solution, routing, manager, locations));
            return routeRepository.save(route);
        } else {
            throw new RuntimeException("No solution found");
        }
    }

    private long calculateDistance(int fromNode, int toNode, List<DeliveryLocation> locations, Depot depot) {
        DeliveryLocation fromLocation = fromNode == 0 ? null : locations.get(fromNode - 1);
        DeliveryLocation toLocation = toNode == 0 ? null : locations.get(toNode - 1);

        double fromLat = (fromLocation != null) ? fromLocation.getLatitude() : depot.getLatitude();
        double fromLon = (fromLocation != null) ? fromLocation.getLongitude() : depot.getLongitude();
        double toLat = (toLocation != null) ? toLocation.getLatitude() : depot.getLatitude();
        double toLon = (toLocation != null) ? toLocation.getLongitude() : depot.getLongitude();

        return (long) haversineDistance(fromLat, fromLon, toLat, toLon);
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // Convert to meters
    }

    private List<DeliveryLocation> mapSolutionToLocations(Assignment solution, RoutingModel routing, RoutingIndexManager manager, List<DeliveryLocation> locations) {
        List<DeliveryLocation> result = new ArrayList<>();
        long index = routing.start(0);
        while (!routing.isEnd(index)) {
            int nodeIndex = manager.indexToNode(index);
            if (nodeIndex != 0) { // skip the depot itself
                result.add(locations.get(nodeIndex - 1));
            }
            index = solution.value(routing.nextVar(index));
        }
        return result;
    }

    @Override
    public Route getRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found for ID: " + routeId));
    }
}
