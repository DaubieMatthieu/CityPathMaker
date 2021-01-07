package main.java.graph;

import java.util.LinkedList;
import java.util.List;

public class Vertex<U, V> {
    private final U label;
    private final List<Edge<U, V>> edges = new LinkedList<>();

    Vertex(U label) {
        this.label = label;
    }

    public Edge<U, V> getEdgeTo(Vertex<U, V> v) {
        for (Edge<U, V> edge : edges) if (edge.getDestination() == v) return edge;
        return null;
    }

    public int getDegree() {
        return edges.size();
    }

    public boolean hasEdge(Edge<U, V> e) {
        return (edges.contains(e));
    }

    public void addEdge(Edge<U, V> e) {
        edges.add(e);
    }

    public U getLabel() {
        return label;
    }

    public List<Edge<U, V>> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(label.toString()).append(": ");
        for (Edge<U, V> edge : edges) stringBuilder.append(edge.toString());
        return stringBuilder.toString();
    }

    public boolean equals(Vertex<U, V> v) {
        return (v.getLabel().equals(label));
    }
    //label is the identifier of a vertex, should not check edges
}
