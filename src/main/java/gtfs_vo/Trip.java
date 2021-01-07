package main.java.gtfs_vo;

import com.opencsv.bean.CsvBindByName;

import java.util.HashMap;

public class Trip extends GenericGtfsVO {
    private static final HashMap<Integer, Trip> trips = new HashMap<>();
    @CsvBindByName(column = "service_id", required = true)
    protected int serviceId;
    @CsvBindByName(column = "trip_id", required = true)
    protected int id;
    @CsvBindByName(column = "trip_headsign")
    protected String tripHeadSign;
    @CsvBindByName(column = "direction_id")
    protected int directionId;
    @CsvBindByName(column = "block_id")
    protected String blockId;
    @CsvBindByName(column = "shape_id")
    protected String shapeId;
    protected Route route;
    @CsvBindByName(column = "route_id", required = true)
    private int routeId;

    public static Trip getTrip(int id) {
        return trips.get(id);
    }

    public static HashMap<Integer, Trip> getAllTrips() {
        return trips;
    }

    @Override
    public void register() {
        trips.put(id, this);
        this.route = Route.getRoute(routeId);
    }

    @Override
    public String toString() {
        return "Direction " + directionId + " on " + route + " (" + id + ")";
    }
}
