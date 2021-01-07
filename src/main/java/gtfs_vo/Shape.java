package main.java.gtfs_vo;

import com.opencsv.bean.CsvBindByName;

import java.util.HashMap;

public class Shape extends GenericGtfsVO {
    private final static HashMap<Integer, Shape> shapes = new HashMap<>();
    @CsvBindByName(column = "shape_id", required = true)
    protected int id;
    @CsvBindByName(column = "shape_pt_lat", required = true)
    protected double latitude;
    @CsvBindByName(column = "shape_pt_lon", required = true)
    protected double longitude;
    @CsvBindByName(column = "shape_pt_sequence", required = true)
    protected int sequence;
    @CsvBindByName(column = "shape_dist_traveled")
    protected double direction;

    @Override
    public void register() {
        shapes.put(id, this);
    }

    @Override
    public String toString() {
        return "Shape " + id;
    }

    public Shape getShape(int id) {
        return shapes.get(id);
    }
}
