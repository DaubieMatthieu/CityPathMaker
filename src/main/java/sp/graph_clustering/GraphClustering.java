package main.java.sp.graph_clustering;

import main.java.helpers.utilityHelper;
import main.java.sp.graph.Edge;
import main.java.sp.graph.Graph;
import main.java.sp.graph.Vertex;
import main.java.sp.sp_algorithms.DijkstraSP;
import main.java.sp.sp_algorithms.Path;

import java.text.DecimalFormat;
import java.util.*;

public class GraphClustering<V, E> {
    private final Graph<V, E> graph;
    private final HashMap<Vertex<V, E>, HashMap<Vertex<V, E>, Path<V, E>>> graphPaths = new HashMap<>();
    private final HashMap<Edge<V, E>, Integer> edgeBetweennesses = new HashMap<>();
    private final LinkedList<Edge<V, E>> clusterBridges = new LinkedList<>();
    private final Set<Cluster<V, E>> clusters = new HashSet<>();

    public GraphClustering(Graph<V, E> graph) {
        this.graph = graph;
        scanGraph();
    }

    //create a path between every node of the graph and
    // calculates edge betweenness of each edge in the graph
    private void scanGraph() {
        long start = System.currentTimeMillis();
        Collection<Vertex<V, E>> vertexes = graph.getVertexes();
        int count=0;
        double total = vertexes.size()*vertexes.size();
        for (Vertex<V, E> source : vertexes) {
            for (Vertex<V, E> destination : vertexes) {
                printProgression(count,total);
                if (count == total/4 || count == total/2 || count == 3*total/4) {
                    System.out.println("");
                    printDuration(start, System.currentTimeMillis());
                }
                createPath(source, destination, new LinkedList<>());
                count++;
            }
        }
        long end = System.currentTimeMillis();
        printDuration(start, end);
    }

    public void printProgression(double count, double total) {
        System.out.print(new DecimalFormat("#.##").format(count/total*100)+"%\r");
    }

    public void printDuration(long start, long end) {
        System.out.println(utilityHelper.timeToHMS((end-start)/1000));
    }



    //recursively calculates the shortest path between source and destination
    //then store the computed path in the graphPaths Map
    //all recursively calculated path will also be stored for reuse
    //don't call the except Vertex, which is the vertex calling this method
    //if this parent vertex was not excepted, the algorithm would loop itself indefinitely
    private void createPath(Vertex<V, E> source, Vertex<V, E> destination, List<Vertex<V,E>> except) {
        if (source==destination) return;
        if (!graphPaths.containsKey(source)) graphPaths.put(source, new HashMap<>());
        if (graphPaths.get(source).containsKey(destination)) return; //this path has already been computed
        HashSet<Path<V, E>> paths = new HashSet<>();
        List<Vertex<V,E>> newExcept = new LinkedList<>(except);
        newExcept.add(source);
        for (Edge<V, E> edge : source.getEdges()) {
            Vertex<V, E> neighbourVertex = edge.getDestination();
            if (except.contains(neighbourVertex)) break; //would create an infinite loop
            Path<V, E> neighbourVertexPath;
            if (neighbourVertex == destination) {
                neighbourVertexPath = new Path<>(graph, neighbourVertex);
            } else {
                createPath(neighbourVertex, destination, newExcept);
                neighbourVertexPath = graphPaths.get(neighbourVertex).get(destination);
            }
            if (neighbourVertexPath != null) {
                paths.add(new Path<>(neighbourVertexPath, source));
            }
        }
        double bestCost = Double.MAX_VALUE;
        Path<V, E> bestPath = null;
        for (Path<V, E> path : paths) {
            if (path.getCost() < bestCost) {
                bestCost = path.getCost();
                bestPath = path;
            }
        }
        if (bestPath==null) {
            if (new DijkstraSP<>(graph, source, destination).getPath().isPath()) {
                //throw new IllegalStateException("path not found while path");
            }
        }
        graphPaths.get(source).put(destination, bestPath);
        computeEdgeBetweeness(source,destination);
    }

    public void computeEdgeBetweeness(Vertex<V, E> source, Vertex<V, E> destination) {
        Path<V, E> path = graphPaths.get(source).get(destination);
        if (path == null) return;
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex<V, E> s = path.get(i);
            Vertex<V, E> d = path.get(i + 1);
            Edge<V, E> link = s.getEdgeTo(d);
            int edgeBetweenness = edgeBetweennesses.getOrDefault(link, 0);
            edgeBetweennesses.put(link, edgeBetweenness + 1);
        }
    }

    public void pause() {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();
    }

    public void checkEB() {
        System.out.println(edgeBetweennesses.size());
        System.out.println(graph.getEdges().size());
        for (int eb: edgeBetweennesses.values()) {
            if (eb==0) throw new IllegalStateException("null eb");
        }
    }

    //recursively creates cluster, by taking the next bridge
    //and breaking the cluster containing it into two subclusters,
    //each one containing a side of the bridge
    public Set<Cluster<V, E>> getClusters(int clusterNumber) {
        //base cluster is the whole graph
        clusters.add(new Cluster<>(graph.getVertexes()));
        while (clusters.size() < clusterNumber) {
            Edge<V, E> bridge = getNextClusterBridge();
            createSubClustersFrom(bridge);
        }
        return clusters;
    }

    private void createSubClustersFrom(Edge<V, E> bridge) {
        Vertex<V, E> destination = bridge.getDestination();
        Vertex<V, E> source = graph.getParentVertex(bridge);
        Cluster<V, E> parentCluster = getClusterContaining(bridge);
        Cluster<V, E> sourceCluster = new Cluster<>(source);
        Cluster<V, E> destinationCluster = new Cluster<>(destination);
        Cluster<V, E> isolatedVertexesCluster = new Cluster<>();
        clusters.remove(parentCluster);
        clusters.add(sourceCluster);
        clusters.add(destinationCluster);
        assert parentCluster != null;
        for (Vertex<V, E> vertex : parentCluster) {
            Path<V, E> pathToSource = graphPaths.get(vertex).get(source);
            Path<V, E> pathToDestination = graphPaths.get(vertex).get(destination);
            if (pathToDestination == null) {
                if (pathToSource == null) {
                    isolatedVertexesCluster.add(vertex);
                } else {
                    sourceCluster.add(vertex);
                }
            } else if (pathToSource == null) {
                isolatedVertexesCluster.add(vertex);
            } else {
                if (pathToSource.getCost() > pathToDestination.getCost()) {
                    destinationCluster.add(vertex);
                } else {
                    sourceCluster.add(vertex);
                }
            }
        }
        if (!isolatedVertexesCluster.isEmpty()) clusters.add(isolatedVertexesCluster);
    }

    //return next bridge (edge with the highest betweenness not already in the clusterBridges)
    private Edge<V, E> getNextClusterBridge() {
        Edge<V, E> lastBridge = (clusterBridges.isEmpty()) ? null : clusterBridges.getLast();
        Edge<V, E> nextBridge = null;
        System.out.println(lastBridge);
        int maxBetweenness = 0;
        for (Edge<V, E> edge : edgeBetweennesses.keySet()) {
            if (edge == lastBridge) break;
            int betweenness = edgeBetweennesses.get(edge);
            if (betweenness > maxBetweenness) {
                nextBridge = edge;
                maxBetweenness = betweenness;
            }
        }
        System.out.println(nextBridge+" : "+edgeBetweennesses.get(nextBridge));
        clusterBridges.addLast(nextBridge);
        return nextBridge;
    }

    private Cluster<V, E> getClusterContaining(Edge<V, E> edge) {
        for (Cluster<V, E> cluster : clusters) if (cluster.hasEdge(edge)) return cluster;
        return null;
    }
}

