package main.java.sp.graph_clustering;

import main.java.sp.graph.Edge;
import main.java.sp.graph.Vertex;

import java.util.Collection;
import java.util.HashSet;

public class Cluster<V, E> extends HashSet<Vertex<V, E>> {
    Vertex<V, E> entryPoint;

    Cluster() {
        this.entryPoint = null;
    }

    Cluster(Vertex<V, E> entryPoint) {
        super();
        this.entryPoint = entryPoint;
    }

    Cluster(Collection<Vertex<V, E>> vertexes) {
        super(vertexes);
        this.entryPoint = null;
    }

    boolean hasEdge(Edge<V, E> edge) {
        return contains(edge.getDestination());
    }
}
