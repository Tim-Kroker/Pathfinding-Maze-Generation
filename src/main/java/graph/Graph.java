package graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Graph<T> {
    private Collection<Node<T>> nodes;

    public Graph() {
        nodes = new HashSet<>();
    }

    public Collection<Node<T>> getNodes() {return nodes;}
    public void addNodes(Collection<Node<T>> nodes) {
        this.nodes.addAll(nodes);
    }
    public void addNode(Node<T> node) {
        this.nodes.add(node);
    }
    public void addEdges(Collection<Edge<T>> edges) {
        edges.forEach(edge -> edge.getNode0().addEdge(edge));
    }
    public void addEdge(Edge<T> edge) {
        edge.getNode0().addEdge(edge);
    }
    public void addEdge(Node<T> node0, Node<T> node1) {
        node0.edgeTo(node1);
    }
    public String toString() {
        StringBuilder list = new StringBuilder();
        nodes.forEach(node -> list.append(node.allInfo()).append("\n\n"));
        return list.toString();
    }
}
