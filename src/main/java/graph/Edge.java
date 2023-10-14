package graph;

import java.util.Objects;

public class Edge<T> {

    private Node<T> node0;
    private Node<T> node1;

    public Edge(Node<T> node0, Node<T> node1) {
        this.node0 = node0;
        this.node1 = node1;
    }

    public Node<T> getNode0() {return node0;}
    public Node<T> getNode1() {return node1;}

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Edge) {
            Edge edge = (Edge) obj;
            return (node0.equals(edge.node0) && node1.equals(edge.node1)) || (node0.equals(node1) && node1.equals(node0));
        }
        return false;
    }

    public String toString() {
        return "Edge{"+node0.toString()+", "+node1.toString()+"}";
    }
}
