package main.java.sp.graph;

import java.util.HashSet;
import java.util.Set;

public class Vertex<V, E> {
    private final V label;
    private final Set<Edge<V, E>> edges = new HashSet<>();

    Vertex(V label) {
        this.label = label;
    }

    public Edge<V, E> getEdgeTo(Vertex<V, E> v) {
        for (Edge<V, E> edge : edges) if (edge.getDestination() == v) return edge;
        return null;
    }

    public int getDegree() {
        return edges.size();
    }

    public boolean hasEdge(Edge<V, E> e) {
        return (edges.contains(e));
    }

    public void addEdge(Edge<V, E> e) {
        edges.add(e);
    }

    public V getLabel() {
        return label;
    }

    public Set<Edge<V, E>> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(label.toString()).append(": ");
        for (Edge<V, E> edge : edges) stringBuilder.append(edge.toString());
        return stringBuilder.toString();
    }

    public boolean equals(Vertex<V, E> v) {
        return (v.getLabel().equals(label));
    }
    //label is the identifier of a vertex, should not check edges
}
