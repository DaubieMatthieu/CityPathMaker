package main.java.sp.graph;

public class Edge<V, E> {
    private final E label;
    private final Vertex<V, E> destination;
    private final double weight;

    public Edge(E label, Vertex<V, E> destination, double weight) {
        this.label = label;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        String label = destination.getLabel().toString();
        return "(" + label + ":" + weight + ") ";
    }

    public Vertex<V, E> getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public E getLabel() {
        return label;
    }
}
