package main.java.sp.sp_algorithms;

import javafx.util.Pair;
import main.java.sp.graph.Vertex;

public class Node<V, E> extends Pair<Vertex<V, E>, Double> implements Comparable<Node<V, E>> {

    public Node(Vertex<V, E> vertex, double cost) {
        super(vertex, cost);
    }

    @Override
    public int compareTo(Node<V, E> o) {
        if (this.getValue() > o.getValue()) return 1;
        if (this.getValue().equals(o.getValue())) return 0;
        return -1;
    }

    public Vertex<V, E> getVertex() {
        return getKey();
    }

    public Double getCost() {
        return getValue();
    }
}