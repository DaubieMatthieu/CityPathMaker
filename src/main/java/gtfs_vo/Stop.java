package main.java.gtfs_vo;


import com.opencsv.bean.CsvBindByName;

import java.util.HashMap;

public class Stop extends GenericGtfsVO {
    private static final HashMap<Integer, Stop> stops = new HashMap<>();
    @CsvBindByName(column = "stop_id", required = true)
    protected int id;
    @CsvBindByName(column = "stop_code")
    protected String code;
    @CsvBindByName(column = "stop_name", required = true) //actually optional but only for locationType=3/4
    protected String name;
    @CsvBindByName(column = "stop_desc")
    protected String description;
    @CsvBindByName(column = "stop_lat", required = true) //same
    protected double latitude;
    @CsvBindByName(column = "stop_lon", required = true) //same
    protected double longitude;
    @CsvBindByName(column = "zone_id")
    protected String zoneId;
    @CsvBindByName(column = "stop_url")
    protected String stopUrl;
    @CsvBindByName(column = "location_type")
    protected int locationType;
    @CsvBindByName(column = "parent_station")
    protected String parentStation;
    //if locationType==0 -> connected to parent station

    public static Stop getStop(int id) {
        return stops.get(id);
    }

    @Override
    public void register() {
        stops.put(id, this);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAddress() { return (description.isEmpty()?latitude+","+longitude:description); }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
