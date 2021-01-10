package main.java.sp.sp_algorithms;

import main.java.sp.graph.Edge;
import main.java.sp.graph.Graph;
import main.java.sp.graph.Vertex;

import java.util.PriorityQueue;

public class DijkstraSP<V, E> extends SPAlgorithm<V, E> {

    public DijkstraSP(Graph<V, E> graph, V source, V destination) {
        super(graph, source, destination);
    }

    public DijkstraSP(Graph<V, E> graph, Vertex<V, E> source, Vertex<V, E> destination) {
        super(graph, source, destination);
    }

    @Override
    protected void computeDirectedTree() {
        directedTree = new DirectedTree<>();
        //using a priority queue allows to explore the graph from the least expensive non visited node
        PriorityQueue<Node<V, E>> nodesToVisit = new PriorityQueue<>();
        Node<V, E> sourceNode = new Node<>(source, 0);
        //the source node is its own parent node
        directedTree.put(source, sourceNode);
        nodesToVisit.add(sourceNode);
        whileLoop:
        while (!nodesToVisit.isEmpty()) {
            Node<V, E> parentNode = nodesToVisit.poll();
            Vertex<V, E> parentVertex = parentNode.getVertex();
            for (Edge<V, E> edge : parentVertex.getEdges()) {
                Vertex<V, E> childVertex = edge.getDestination();
                Double weight = edge.getWeight();
                Double cost = parentNode.getCost() + weight;
                if (directedTree.containsKey(childVertex)) {
                    //if accessing the child node is less expensive from this node,
                    //we replace the parent node of this child node in the directed tree
                    if (directedTree.get(childVertex).getCost() > cost) {
                        directedTree.put(childVertex, parentNode);
                    }
                } else {
                    directedTree.put(childVertex, new Node<>(parentVertex, cost));
                    Node<V, E> childNode = new Node<>(childVertex, cost);
                    nodesToVisit.add(childNode);
                }
                if (childVertex.equals(destination)) break whileLoop;
            }
        }
    }
}
