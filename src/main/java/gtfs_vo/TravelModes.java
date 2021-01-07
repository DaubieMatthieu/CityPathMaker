package main.java.gtfs_vo;

import java.util.HashMap;

public final class TravelModes {
    private static final HashMap<Integer, String> correspondences = new HashMap<Integer, String>() {{
        put(0, "tram");
        put(1, "subway");
        put(2, "rail");
        put(3, "bus");
        put(4, "ferry");
        put(5, "cablecar");
        put(6, "gondola");
        put(7, "funicular");
        put(11, "trolleybus");
        put(12, "monorail");
    }};

    public static String getTag(int routeType) {
        return correspondences.get(routeType);
    }
}
