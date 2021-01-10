package main.java.sp.sp_algorithms;

import main.java.sp.graph.Vertex;

import java.util.HashMap;


//the directed tree is a tool to visit a graph vertexes,
//nodes are representation of the vertexes for the directed tree (it's a couple (vertex,cost))
//for each entry, the key is a vertex, and the value is the parent node in the directed tree
public class DirectedTree<V, E> extends HashMap<Vertex<V, E>, Node<V, E>> {
}
