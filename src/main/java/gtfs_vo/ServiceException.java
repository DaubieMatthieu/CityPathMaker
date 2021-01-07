package main.java.gtfs_vo;

import com.opencsv.bean.CsvBindByName;

public class ServiceException extends GenericGtfsVO {
    @CsvBindByName(column = "date", required = true)
    protected String date;
    @CsvBindByName(column = "exception_type", required = true)
    protected String exceptionType;
    protected Service service;
    @CsvBindByName(column = "service_id", required = true)
    private int serviceId;

    @Override
    public void register() {
        this.service = Service.getService(serviceId);
    }

    @Override
    public String toString() {
        return service + " excepted on " + date;
    }
}
