package main.java.gtfs_vo;

import com.opencsv.bean.CsvBindByName;

import java.util.HashMap;

public class Route extends GenericGtfsVO {
    private static final HashMap<Integer, Route> routes = new HashMap<>();
    @CsvBindByName(column = "route_id", required = true)
    protected int id;
    @CsvBindByName(column = "agency_id")
    protected String agencyId;
    @CsvBindByName(column = "route_short_name") //short or long should be defined
    protected String shortName;
    @CsvBindByName(column = "route_long_name") //short or long should be defined
    protected String longName;
    @CsvBindByName(column = "route_desc")
    protected String description;
    @CsvBindByName(column = "route_type", required = true)
    protected int type;
    @CsvBindByName(column = "route_url")
    protected String url;
    @CsvBindByName(column = "route_color")
    protected String color;
    @CsvBindByName(column = "route_text_color")
    protected String textColor;

    public static Route getRoute(int id) {
        return routes.get(id);
    }

    @Override
    public void register() {
        routes.put(id, this);
    }

    @Override
    public String toString() {
        String name = (shortName.isEmpty()) ? longName : shortName;
        return TravelModes.getTag(type) + " " + shortName;
    }
}
