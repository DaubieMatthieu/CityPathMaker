package main.java.algorithm;

import javafx.util.Pair;
import main.java.graph.Edge;
import main.java.graph.Graph;
import main.java.graph.Vertex;

import java.util.*;

public class DijkstraSP<U, V> {
    private Graph<U, V> graph;
    private Vertex<U, V> source;
    private Vertex<U, V> destination;
    private HashMap<Vertex<U, V>, Pair<Vertex<U, V>, Double>> directedTree;
    private LinkedList<Vertex<U, V>> vertexPath;
    private boolean validEntry;
    private boolean isPath;
    private Double cost;

    public DijkstraSP(Graph<U, V> graph, U source, U destination) {
        compute(graph, source, destination);
    }

    public DijkstraSP() {
        validEntry = false;
    }

    public void compute(Graph<U, V> graph, U source, U destination) {
        this.graph = graph;
        this.source = graph.getVertex(source);
        this.destination = graph.getVertex(destination);
        validEntry = (this.source != null && this.destination != null);
        if (!validEntry) return;
        computeDirectedTree();
        isPath = directedTree.containsKey(this.destination);
        if (!isPath) return;
        computeVertexPath();
        cost = directedTree.get(this.destination).getValue();
    }

    private void computeDirectedTree() {
        System.out.println("Computing path in " + graph + " with Dijkstra algorithm..");
        directedTree = new HashMap<>();
        //visitedVertex -> (parentVertex, cost)
        Queue<Vertex<U, V>> vertexesToVisit = new ArrayDeque<>();
        directedTree.put(source, new Pair<>(source, 0.0));
        vertexesToVisit.add(source);
        while (!vertexesToVisit.isEmpty()) {
            Vertex<U, V> vertex = vertexesToVisit.poll();
            for (Edge<U, V> edge : vertex.getEdges()) {
                Vertex<U, V> neighbourVertex = edge.getDestination();
                Double weight = edge.getWeight();
                Double cost = directedTree.get(vertex).getValue() + weight;
                Pair<Vertex<U, V>, Double> pair = new Pair<>(vertex, cost);
                if (directedTree.containsKey(neighbourVertex)) {
                    if (directedTree.get(neighbourVertex).getValue() > cost) {
                        directedTree.put(neighbourVertex, pair);
                    }
                } else {
                    directedTree.put(neighbourVertex, pair);
                    vertexesToVisit.add(neighbourVertex);
                }
                if (neighbourVertex.equals(destination)) break;
            }
        }
    }

    private void computeVertexPath() {
        vertexPath = new LinkedList<>();
        vertexPath.add(destination);
        Vertex<U, V> targetVertex = destination;
        while (!targetVertex.equals(source)) {
            Vertex<U, V> parentVertex = directedTree.get(targetVertex).getKey();
            vertexPath.addFirst(parentVertex);
            targetVertex = parentVertex;
        }
    }

    public void printSP() {
        if (!validEntry) {
            System.out.println("The given data was invalid");
            return;
        }
        if (!isPath) {
            System.out.println("The destination '" + destination.getLabel() + "' is unreachable from '" + source.getLabel() + "' in " + graph);
            return;
        }
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        for (int i = 0; i < vertexPath.size() - 1; i++) {
            Vertex<U, V> source = vertexPath.get(i);
            Vertex<U, V> destination = vertexPath.get(i + 1);
            Edge<U, V> link = source.getEdgeTo(destination);
            sj.add("    " + source.getLabel() + " => " + link.getLabel() + " => " + destination.getLabel());
        }
        System.out.println("Shortest Path from '" + source.getLabel() + "' to '" + destination.getLabel() + "' in " + graph + " is:");
        System.out.println(sj.toString());
        System.out.println("cost=" + cost);
    }

    public LinkedList<Vertex<U, V>> getVertexPath() {
        return (validEntry && isPath) ? vertexPath : null;
    }

    public double getCost() {
        return (validEntry && isPath) ? cost : 0;
    }

    public Graph<U, V> getGraph() {
        return graph;
    }

    public Vertex<U, V> getSource() {
        return source;
    }

    public Vertex<U, V> getDestination() {
        return destination;
    }
}
