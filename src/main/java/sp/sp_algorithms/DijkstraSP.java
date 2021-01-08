package main.java.sp.sp_algorithms;

import javafx.util.Pair;
import main.java.sp.graph.Edge;
import main.java.sp.graph.Graph;
import main.java.sp.graph.Vertex;

import java.util.ArrayDeque;
import java.util.Queue;

public class DijkstraSP<V, E> extends SPAlgorithm<V, E> {

    public DijkstraSP(Graph<V, E> graph, V source, V destination) {
        super(graph, source, destination);
    }

    @Override
    protected void computeDirectedTree() {
        directedTree = new DirectedTree<>();
        //visitedVertex -> (parentVertex, cost)
        Queue<Vertex<V, E>> vertexesToVisit = new ArrayDeque<>();
        directedTree.put(source, new Pair<>(source, 0.0));
        vertexesToVisit.add(source);
        while (!vertexesToVisit.isEmpty()) {
            Vertex<V, E> vertex = vertexesToVisit.poll();
            for (Edge<V, E> edge : vertex.getEdges()) {
                Vertex<V, E> neighbourVertex = edge.getDestination();
                Double weight = edge.getWeight();
                Double cost = directedTree.get(vertex).getValue() + weight;
                Pair<Vertex<V, E>, Double> pair = new Pair<>(vertex, cost);
                if (directedTree.containsKey(neighbourVertex)) {
                    if (directedTree.get(neighbourVertex).getValue() > cost) {
                        directedTree.put(neighbourVertex, pair);
                    }
                } else {
                    directedTree.put(neighbourVertex, pair);
                    vertexesToVisit.add(neighbourVertex);
                }
                if (neighbourVertex.equals(destination)) break;
            }
        }
    }
}
