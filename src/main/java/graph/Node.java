package graph;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Node<T> {

    private T identifier;
    private Collection<Node<T>> adjacentNodes;

    public Node(T identifier) {
        this.identifier = identifier;
        adjacentNodes = new LinkedList<>();
        System.out.println("New Node");
    }

    public void addEdge(Edge<T> edge) {
        adjacentNodes.add(edge.getNode1());
    }
    public void edgeTo(Node<T> node) {
        adjacentNodes.add(node);
//        System.out.println("Adjacent");
    }

    public T get() {return identifier;}
    public Collection<Node<T>> getAdjacentNodes() {return adjacentNodes;}
    public Collection<Edge<T>> getAdjacentEdges() {
        LinkedList<Edge<T>> edges = new LinkedList<>();
        adjacentNodes.forEach(node -> edges.add(new Edge<T>(this, node)));
//        System.out.println("Adjacent Nodes");
        return edges;
    }
    public String toString() {
        return "Node[identifier = "+identifier.toString()+"]";
    }
    public String allInfo() {
        StringBuilder neighbors = new StringBuilder();
        getAdjacentEdges().forEach(edge -> {
            neighbors.append('\t').append(edge.toString()).append("\n");
            System.out.println("Neughbor");
        });
        return this + ": \n"+neighbors+"\n";
    }



}
