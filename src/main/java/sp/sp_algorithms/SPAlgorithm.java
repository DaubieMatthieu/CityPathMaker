package main.java.sp.sp_algorithms;

import main.java.sp.graph.Graph;
import main.java.sp.graph.Vertex;

public abstract class SPAlgorithm<V, E> {
    protected final Graph<V, E> graph;
    protected final Vertex<V, E> source;
    protected final Vertex<V, E> destination;
    protected DirectedTree<V, E> directedTree;
    protected Path<V, E> path;


    public SPAlgorithm(Graph<V, E> graph, V source, V destination) {
        this(graph, graph.getVertex(source), graph.getVertex(destination));
    }

    public SPAlgorithm(Graph<V, E> graph, Vertex<V, E> source, Vertex<V, E> destination) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        if (this.source == null) throw new IllegalArgumentException("Source " + source + " not in graph " + graph);
        if (this.destination == null)
            throw new IllegalArgumentException("Destination " + destination + " not in graph " + graph);
        computePath();
    }

    protected void computePath() {
        computeDirectedTree();
        path = new Path<>(this);
    }

    protected abstract void computeDirectedTree();

    public Path<V, E> getPath() {
        return path;
    }

    public String toString() {
        return "Algorithm " + getName() + " to find shortest path between " + source.getLabel() + " and " + destination.getLabel() + " in " + graph;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
}
