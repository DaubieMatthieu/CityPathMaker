package main.java.sp.graph;

import java.util.*;
// Generic weighted Graph implementation
//U is the type of the Vertexes and V the type of the Edges
// is weighted but with default value 1, so it can be used without weight

public class Graph<V, E> {
    protected final static HashMap<String, Graph> instances = new HashMap<>(); //TODO can we remove this ?
    protected final String instanceName;
    protected final HashMap<V, Vertex<V, E>> vertexes = new HashMap<>();
    protected final boolean directed;
    protected final boolean weighted;

    public Graph(boolean directed, boolean weighted) {
        this(String.valueOf(instances.size()), directed, weighted);
    }

    public Graph(String name, boolean directed, boolean weighted) {
        instanceName = name;
        instances.put(instanceName, this);
        this.directed = directed;
        this.weighted = weighted;
    }

    public Graph(List<V> sources, List<V> destinations, boolean directed) {
        this(String.valueOf(instances.size()), sources, destinations, directed);
    }

    public Graph(String name, List<V> sources, List<V> destinations, boolean directed) {
        this(sources, destinations, new LinkedList<>(), new LinkedList<>(), directed);
    }

    public Graph(List<V> sourceVertexes, List<V> destinationsVertexes, List<E> links, List<Double> weights, boolean directed) {
        this(String.valueOf(instances.size()), sourceVertexes, destinationsVertexes, links, weights, directed);
    }

    public Graph(String name, List<V> sourceVertexes, List<V> destinationsVertexes, List<E> links, List<Double> weights, boolean directed) {
        instanceName = name;
        instances.put(instanceName, this);
        this.directed = directed;
        this.weighted = !weights.isEmpty();
        for (int i = 0; i < sourceVertexes.size(); i++) {
            V source = sourceVertexes.get(i);
            V destination = destinationsVertexes.get(i);
            E link = links.get(i);
            double weight = (weighted) ? weights.get(i) : 1.0;
            addEdge(source, destination, link, weight);
        }
    }

    public void addEdge(V source, V destination, E link, double weight) {
        Vertex<V, E> s = getVertex(source);
        Vertex<V, E> d = getVertex(destination);
        if (s == null) {
            s = new Vertex<>(source);
            vertexes.put(source, s);
        }
        if (d == null) {
            d = new Vertex<>(destination);
            vertexes.put(destination, d);
        }
        s.addEdge(new Edge<>(link, d, weight));
        if (!directed) d.addEdge(new Edge<>(link, s, weight));
    }

    public Vertex<V, E> getVertex(V label) {
        return vertexes.get(label);
    }

    public int getSize() {
        return vertexes.size();
    }

    public int getOrder() {
        int order = 0;
        for (Vertex<V, E> v : vertexes.values()) order += v.getDegree();
        return order;
    }

    public Set<Edge<V, E>> getEdges() {
        Set<Edge<V, E>> edges = new HashSet<>();
        for (Vertex<V, E> v : vertexes.values()) edges.addAll(v.getEdges());
        return edges;
    }

    public boolean verifyNonNegative() {
        for (Edge<V, E> e : getEdges()) if (e.getWeight() < 0) return false;
        return true;
    }

    public void print() {
        print(false);
    }

    public void print(boolean verbose) {
        if (verbose) {
            String description = "";
            description += (weighted) ? "weighted " : "unweighted ";
            description += (directed) ? "directed " : "bidirectional ";
            description += toString();
            System.out.println(description);
            System.out.println("Size: " + getSize());
            System.out.println("Order: " + getOrder());
            System.out.println((verifyNonNegative() ? "is non negative" : "has negative weights"));
        }
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        for (Vertex<V, E> v : vertexes.values()) sj.add(v.toString());
        System.out.println(sj.toString());
    }

    @Override
    public String toString() {
        return "graph " + instanceName;
    }
}