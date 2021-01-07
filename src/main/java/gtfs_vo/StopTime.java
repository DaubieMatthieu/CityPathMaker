package main.java.gtfs_vo;

import com.opencsv.bean.CsvBindByName;

import java.util.HashMap;

public class StopTime extends GenericGtfsVO {
    private final static HashMap<Integer, HashMap<Integer, StopTime>> stopTimes = new HashMap<>();
    @CsvBindByName(column = "arrival_time")
    protected String arrivalTime;
    @CsvBindByName(column = "departure_time")
    protected String departureTime;
    @CsvBindByName(column = "stop_sequence", required = true)
    protected int stopSequence;
    @CsvBindByName(column = "stop_headsign")
    protected String stopHeadSign;
    @CsvBindByName(column = "shape_dist_traveled")
    protected double shapeDistanceTraveled;
    protected Stop stop;
    protected Trip trip;
    @CsvBindByName(column = "trip_id", required = true)
    private int tripId;
    @CsvBindByName(column = "stop_id", required = true)
    private int stopId;

    public static StopTime getStopTime(int tripId, int stopSequence) {
        HashMap<Integer, StopTime> tripSequence = stopTimes.get(tripId);
        return tripSequence.get(stopSequence);
    }

    @Override
    public void register() {
        if (!stopTimes.containsKey(tripId)) {
            stopTimes.put(tripId, new HashMap<>());
        }
        stopTimes.get(tripId).put(stopSequence, this);
        this.stop = Stop.getStop(stopId);
        this.trip = Trip.getTrip(tripId);
    }

    @Override
    public String toString() {
        return "Stop Time for " + stop + " on " + trip;
    }
}
