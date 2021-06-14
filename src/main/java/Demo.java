package main.java;

import main.java.gtfs_vo.Stop;
import main.java.gtfs_vo.StopTime;
import main.java.gtfs_vo.Trip;
import main.java.helpers.googleMapsHelper;
import main.java.helpers.utilityHelper;
import main.java.sp.graph.Edge;
import main.java.sp.graph.Graph;
import main.java.sp.graph.Vertex;
import main.java.sp.sp_algorithms.Path;
import main.java.sp.sp_algorithms.SPAlgorithm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringJoiner;

public class Demo {
    private static final int verboseLevel = 2;
    private static Stop source;
    private static Stop destination;
    private final boolean showMapURI = true;
    private final boolean weighted;
    private final Class<SPAlgorithm<Stop, Trip>> algorithmClass;
    private final Graph<Stop, Trip> graph;

    Demo(boolean weighted, Class<? extends SPAlgorithm> algorithm) {
        this.weighted = weighted;
        //noinspection unchecked
        this.algorithmClass = (Class<SPAlgorithm<Stop, Trip>>) algorithm;
        graph = instantiateGraph(weighted);
        if (source == null) source = utilityHelper.getRandomElement(Stop.getStops());
        if (destination == null) destination = utilityHelper.getRandomElement(Stop.getStops());
        launchTest();
    }

    public static Graph<Stop, Trip> instantiateGraph(boolean weighted) {
        if (verboseLevel >= 2) System.out.println("Instantiating graph");
        HashMap<Integer, Trip> trips = Trip.getAllTrips();
        Graph<Stop, Trip> graph = new Graph<>("Albuquerque map", false, weighted);
        int tripsNumber = trips.size();
        int count = 1;
        if (verboseLevel >= 2) System.out.println("    Adding " + tripsNumber + " trips to the graph");
        for (Trip trip:trips.values()) {
            assert trip != null;
            if (verboseLevel >= 3) System.out.printf("    Adding trip %d to graph (%d/%d)\r", trip.getId(), count, tripsNumber);
            int stopSequence = 1;
            while (StopTime.getStopTime(trip.getId(), stopSequence + 1) != null) {
                StopTime stopTime1 = StopTime.getStopTime(trip.getId(), stopSequence);
                StopTime stopTime2 = StopTime.getStopTime(trip.getId(), stopSequence + 1);
                assert stopTime1 != null;
                assert stopTime2 != null;
                Stop stop1 = stopTime1.getStop();
                Stop stop2 = stopTime2.getStop();
                double cost = (weighted) ? utilityHelper.getDistanceBetween(stop1, stop2) : 1;
                graph.addEdge(stop1, stop2, trip, cost);
                stopSequence++;
            }
            count++;
        }
        if (verboseLevel >= 2) System.out.println(System.lineSeparator());
        return graph;
    }

    private static URI getPathURI(Path<Stop, Trip> path) {
        //google maps allows only 23 waypoints -> 25 points in total
        //therefore we reduce the path size to 25 points at most
        Path<Stop, Trip> sizedPath = getReducedPath(path, 25);
        if (sizedPath == null) {
            System.out.println("The path is too long and could not be reduced enough to be shown on Google Maps");
            return null;
        } else {
            if (!sizedPath.equals(path)) {
                System.out.println("The full path is too long for Google Maps," +
                        " so it has been reduced to the main turn points");
            }
            return googleMapsHelper.AddressPathToURI(VertexPathToAddressPath(sizedPath));
        }
    }

    //reducing the path to a <maxSize size
    private static Path<Stop, Trip> getReducedPath(Path<Stop, Trip> fullPath, int maxSize) {
        if (fullPath.size() < 25) return fullPath;
        //creating a reduced version of the full path
        Path<Stop, Trip> reducedPath = new Path<>(fullPath); //cloning the full path
        Trip currentTrip = null;
        for (int i = 1; i < fullPath.size() - 1; i++) {
            Vertex<Stop, Trip> s = fullPath.get(i);
            Vertex<Stop, Trip> d = fullPath.get(i + 1);
            Edge<Stop, Trip> link = s.getEdgeTo(d);
            Trip trip = link.getLabel();
            //keeping only the points where the user is taking a new trip
            if (trip.equals(currentTrip)) reducedPath.remove(s);
            else currentTrip = trip;
        }
        if (reducedPath.size() > maxSize) return null; //failed to reduce the path to the requested size
        return reducedPath;
    }

    private static String[] VertexPathToAddressPath(LinkedList<Vertex<Stop, Trip>> vPath) {
        String[] cPath = new String[vPath.size()];
        for (int i = 0; i < vPath.size(); i++) cPath[i] = vPath.get(i).getLabel().getCoordinates();
        return cPath;
    }

    //act like a class constructor for the static attributes
    //source and destination are the same for every tests
    //will throw error if called after instantiation of tests
    public static void setParameters(int sourceStopId, int destinationStopId) {
        setParameters(Stop.getStop(sourceStopId), Stop.getStop(destinationStopId));
    }

    private static void setParameters(Stop source, Stop destination) {
        if (Demo.source != null) throw new IllegalArgumentException("Parameters have already been set");
        if (Demo.destination != null) throw new IllegalArgumentException("Parameters have already been set");
        Demo.source = source;
        Demo.destination = destination;
    }

    private void launchTest() {
        System.out.println(System.lineSeparator());
        String description = (weighted) ? "Weighted" : "Unweighted";
        description += " graph with " + algorithmClass.getSimpleName() + " algorithm";
        System.out.println(description + System.lineSeparator());
        SPAlgorithm<Stop, Trip> algorithm = getAlgorithmInstance(graph, source, destination);
        assert algorithm != null;
        Path<Stop, Trip> path = algorithm.getPath();
        printPath(path);
        System.out.println("Distance between the two stops: " + utilityHelper.formatDistance(
                utilityHelper.getDistanceBetween(source, destination)));
        if (showMapURI) {
            if (path.isPath())
                System.out.println("You can see the computed path on Google Maps at " + getPathURI(path));
            System.out.println("You can see Google's path at "
                    + googleMapsHelper.getGooglePathURI(source.getCoordinates(), destination.getCoordinates()));
        }
    }

    private void printPath(Path<Stop, Trip> path) {
        Vertex<Stop, Trip> source = path.getSource();
        Vertex<Stop, Trip> destination = path.getDestination();
        double cost = path.getCost();
        if (!path.isPath()) {
            path.print(); //default message when no path is found
            return;
        }
        System.out.println("Shortest Path from '" + source.getLabel() + "' to '" + destination.getLabel() + "' in " + path.getGraph());
        Trip currentTrip = null;
        double travelDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex<Stop, Trip> s = path.get(i);
            Vertex<Stop, Trip> d = path.get(i + 1);
            Edge<Stop, Trip> link = s.getEdgeTo(d);
            Trip trip = link.getLabel();
            if (!trip.equals(currentTrip)) {
                currentTrip = trip;
                if (verboseLevel >= 1) System.out.println("    Take " + trip + " at " + s.getLabel());
            }
            if (verboseLevel >= 3) {
                StringJoiner sj = new StringJoiner(" => ");
                sj.add(s.getLabel().toString());
                sj.add(link.getLabel().toString());
                sj.add(d.getLabel().toString());
                System.out.println("        " + sj.toString());
            }
            travelDistance += utilityHelper.getDistanceBetween(s.getLabel(), d.getLabel());
        }
        assert currentTrip != null;
        if (verboseLevel >= 1)
            System.out.println("    Get out of " + currentTrip.getRoute() + " at " + destination.getLabel());
        System.out.println(System.lineSeparator() + "Number of stops: " + path.size());
        System.out.println("Estimated travel distance: " + utilityHelper.formatDistance(travelDistance));
    }

    private SPAlgorithm<Stop, Trip> getAlgorithmInstance(Graph<Stop, Trip> graph, Stop source, Stop destination) {
        try {
            Constructor<SPAlgorithm<Stop, Trip>> constructor = algorithmClass.getConstructor(Graph.class, Object.class, Object.class);
            return constructor.newInstance(graph, source, destination);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
