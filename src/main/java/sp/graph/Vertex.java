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
        return "Vertex of " + label + " (" + edges.size() + " edges)";
    }

    public boolean equals(Vertex<V, E> v) {
        return (v.getLabel().equals(label));
    }
    //label is the identifier of a vertex, should not check edges
}
