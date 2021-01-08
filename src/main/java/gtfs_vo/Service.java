package main.java.gtfs_vo;

import com.opencsv.bean.CsvBindByName;

import java.util.HashMap;

public class Service extends GenericGtfsVO {
    private static final HashMap<Integer, Service> services = new HashMap<>();

    @CsvBindByName(column = "service_id", required = true)
    protected int id;
    @CsvBindByName(column = "monday", required = true)
    protected boolean activeOnMonday;
    @CsvBindByName(column = "tuesday", required = true)
    protected boolean activeOnTuesday;
    @CsvBindByName(column = "wednesday", required = true)
    protected boolean activeOnWednesday;
    @CsvBindByName(column = "thursday", required = true)
    protected boolean activeOnThursday;
    @CsvBindByName(column = "friday", required = true)
    protected boolean activeOnFriday;
    @CsvBindByName(column = "saturday", required = true)
    protected boolean activeOnSaturday;
    @CsvBindByName(column = "sunday", required = true)
    protected boolean activeOnSunday;
    @CsvBindByName(column = "start_date", required = true)
    protected int startDate;
    @CsvBindByName(column = "end_date", required = true)
    protected int endDate;

    public static Service getService(int id) {
        return services.get(id);
    }

    @Override
    public void register() {
        services.put(id, this);
    }

    @Override
    public String toString() {
        return "Service " + id;
    }

}
