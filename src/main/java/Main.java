package main.java;

import main.java.gtfs_vo.Stop;
import main.java.gtfs_vo.Trip;
import main.java.helpers.gtfsHelper;
import main.java.sp.graph.Graph;
import main.java.sp.graph_clustering.Cluster;
import main.java.sp.graph_clustering.GraphClustering;
import main.java.sp.sp_algorithms.BFS;
import main.java.sp.sp_algorithms.DijkstraSP;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        gtfsHelper.scanResources();
        //launchSPTests();
        launchClusteringTest();
    }

    public static void launchSPTests() {
        //can be called before launching tests to define which itinerary to compute
        //if not, the source and destination stops will be chosen randomly
        //Demo.setParameters(1081, 1082);
        new Demo(false, BFS.class);
        new Demo(true, DijkstraSP.class);
    }

    public static void launchClusteringTest() {
        GraphClustering<Stop, Trip> graphClustering;
        Graph<Stop, Trip> graph = Demo.instantiateGraph(true);
        System.out.println("Calculating the edge betweennesses in "+graph);
        try {
            graphClustering = new GraphClustering<>(graph);
        } catch (StackOverflowError e) {
            System.out.println("Could not show the Graph Clustering results, stack overflow");
            return;
        }
        System.out.println("Calculating the clusters in "+graph);
        Set<Cluster<Stop, Trip>> clusters = graphClustering.getClusters(5);
        for (Cluster<Stop, Trip> cluster : clusters) {
            System.out.println("Cluster: " + cluster.size());
        }
    }
}
