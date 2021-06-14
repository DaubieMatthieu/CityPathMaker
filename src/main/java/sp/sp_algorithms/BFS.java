package main.java.sp.sp_algorithms;

import main.java.sp.graph.Edge;
import main.java.sp.graph.Graph;
import main.java.sp.graph.Vertex;

import java.util.ArrayDeque;

public class BFS<V, E> extends SPAlgorithm<V, E> {

    public BFS(Graph<V, E> graph, V source, V destination) {
        super(graph, source, destination);
    }

    @Override
    protected void computeDirectedTree() {
        directedTree = new DirectedTree<>();
        ArrayDeque<Node<V, E>> nodesToVisit = new ArrayDeque<>();
        Node<V, E> sourceNode = new Node<>(source, 0);
        //the source node is its own parent node
        directedTree.put(source, sourceNode);
        nodesToVisit.add(sourceNode);
        while (!nodesToVisit.isEmpty()) {
            Node<V, E> parentNode = nodesToVisit.poll();
            Vertex<V, E> parentVertex = parentNode.getVertex();
            if (parentVertex.equals(destination)) break;
            for (Edge<V, E> edge : parentVertex.getEdges()) {
                Vertex<V, E> childVertex = edge.getDestination();
                double cost = parentNode.getCost() + 1;
                //if the child vertex has already been visited, it must have been by a shorter path
                //therefore we ignore it
                if (!directedTree.containsKey(childVertex)) {
                    directedTree.put(childVertex, new Node<>(parentVertex, cost));
                    Node<V, E> childNode = new Node<>(childVertex, cost);
                    nodesToVisit.add(childNode);
                }
            }
        }
    }
}
