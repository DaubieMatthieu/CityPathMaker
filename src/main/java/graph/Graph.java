package main.java.graph;

import java.util.*;
// Generic weighted Graph implementation
//U is the type of the Vertexes and V the type of the Edges
// is weighted but with default value 1, so it can be used without weight

public class Graph<U, V> {
    protected static HashMap<String, Graph> instances = new HashMap<>();
    protected final String instanceName;
    protected final ArrayList<Vertex<U, V>> vertexes = new ArrayList<>();
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

    public Graph(List<U> sources, List<U> destinations, boolean directed) {
        this(String.valueOf(instances.size()), sources, destinations, directed);
    }

    public Graph(String name, List<U> sources, List<U> destinations, boolean directed) {
        this(sources, destinations, new LinkedList<>(), new LinkedList<>(), directed);
    }

    public Graph(List<U> sourceVertexes, List<U> destinationsVertexes, List<V> links, List<Double> weights, boolean directed) {
        this(String.valueOf(instances.size()), sourceVertexes, destinationsVertexes, links, weights, directed);
    }

    public Graph(String name, List<U> sourceVertexes, List<U> destinationsVertexes, List<V> links, List<Double> weights, boolean directed) {
        instanceName = name;
        instances.put(instanceName, this);
        this.directed = directed;
        this.weighted = !weights.isEmpty();
        for (int i = 0; i < sourceVertexes.size(); i++) {
            U source = sourceVertexes.get(i);
            U destination = destinationsVertexes.get(i);
            V link = links.get(i);
            double weight = (weighted) ? weights.get(i) : 1.0;
            addEdge(source, destination, link, weight);
        }
    }

    public void addEdge(U source, U destination, V link, double weight) {
        Vertex<U, V> s = getVertex(source);
        Vertex<U, V> d = getVertex(destination);
        if (s == null) {
            s = new Vertex<>(source);
            vertexes.add(s);
        }
        if (d == null) {
            d = new Vertex<>(destination);
            vertexes.add(d);
        }
        s.addEdge(new Edge<>(link, d, weight));
        if (!directed) d.addEdge(new Edge<>(link, s, weight));
    }

    public Vertex<U, V> getVertex(U label) {
        for (Vertex<U, V> v : vertexes) if (v.getLabel().equals(label)) return v;
        return null;
    }

    public void addVertex(Vertex<U, V> v) {
        vertexes.add(v);
    }

    public int getSize() {
        return vertexes.size();
    }

    public int getOrder() {
        int order = 0;
        for (Vertex<U, V> v : vertexes) order += v.getDegree();
        return order;
    }

    public ArrayList<Vertex<U, V>> getVertexes() {
        return vertexes;
    }

    public LinkedList<Edge<U, V>> getEdges() {
        LinkedList<Edge<U, V>> edges = new LinkedList<>();
        for (Vertex<U, V> v : vertexes) {
            for (Edge<U, V> e : v.getEdges()) if (!edges.contains(e)) edges.add(e);
        }
        return edges;
    }

    public boolean verifyNonNegative() {
        for (Edge<U, V> e : getEdges()) if (e.getWeight() < 0) return false;
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
        for (Vertex<U, V> v : vertexes) sj.add(v.toString());
        System.out.println(sj.toString());
    }

    @Override
    public String toString() {
        return "graph " + instanceName;
    }
}