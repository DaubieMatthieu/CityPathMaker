package main.java.helpers;

import main.java.gtfs_vo.Stop;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public interface utilityHelper {
    static double toRadian(double degrees) {
        return (Math.PI * degrees) / 180;
    }

    static double getDistanceBetween(Stop stop1, Stop stop2) {
        double lat1 = toRadian(stop1.getLatitude());
        double lat2 = toRadian(stop2.getLatitude());
        double lon1 = toRadian(stop1.getLongitude());
        double lon2 = toRadian(stop2.getLongitude());
        int radius = 6371;
        double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        double y = lat2 - lat1;
        return radius * Math.sqrt(x * x + y * y);
    }

    static String formatDistance(double distanceInKm) {
        if (distanceInKm < 10) {
            return new DecimalFormat("#").format(distanceInKm * 1000) + "m";
        } else return new DecimalFormat("#.#").format(distanceInKm) + "km";
    }

    static <K, V> V getRandomElement(Map<K, V> map) {
        return map.get(getRandomElement(map.keySet()));
    }

    static <E> E getRandomElement(Set<E> set) {
        int randomIndex = new Random().nextInt(set.size());
        Iterator<E> iterator = set.iterator();
        int index = 0;
        E randomElement = null;
        while (iterator.hasNext()) {
            randomElement = iterator.next();
            if (index == randomIndex) return randomElement;
            index++;
        }
        return randomElement;
    }
}
