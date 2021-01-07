package main.java.graph;

public class Edge<U, V> {
    private final V label;
    private final Vertex<U, V> destination;
    private final double weight;

    public Edge(V label, Vertex<U, V> destination, double weight) {
        this.label = label;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        String label = destination.getLabel().toString();
        return "(" + label + ":" + weight + ") ";
    }

    public Vertex<U, V> getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public V getLabel() {
        return label;
    }
}
