package pathfinding;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

public class MazeGenerator {


    public static void insertMaze(boolean[][] walls, int x, int y, int width, int height) {
        if(x < 0 || y < 0) throw new RuntimeException("X and Y Position cannot be negative");
        if(x + width > walls.length || y + height > walls[0].length) throw new RuntimeException("Maze dimensions exceed grid size");

        if(width % 2 == 0) width--;
        if(height % 2 == 0) height--;

        int junctionCountX = (width-1)/2;
        int junctionCountY = (height-1)/2;
        Graph<Vec2D> junctions = new Graph<>();

        /* Fill Maze with Walls */
        for(int x0 = x; x0 < x + width; x0++) {
            for(int y0 = y; y0 < y + height; y0++) {
                walls[x0][y0] = true;
            }
        }

        /* Create Nodes */
        Node<Vec2D>[] buffer = new Node[junctionCountX];
        Node<Vec2D> previous;
        for(int y0 = 0; y0 < junctionCountY; y0++) {
            previous = null;
            for(int x0 = 0; x0 < junctionCountX; x0++) {
                Node<Vec2D> node = new Node<>(new Vec2D(x0,y0));
                junctions.addNode(node);
                if(previous != null) {
                    previous.edgeTo(node);
                    node.edgeTo(previous);
                }
                if(buffer[x0] != null) {
                    buffer[x0].edgeTo(node);
                    node.edgeTo(buffer[x0]);
                }
                buffer[x0] = node;
                previous = node;
            }
        }

        /* Prim algo */
        Collection<Node<Vec2D>> nodes = junctions.getNodes(); //Nodes which have to be connected
        Node<Vec2D> startingNode = nodes.stream().findAny().get();
        nodes.remove(startingNode);
        List<Edge<Vec2D>> adjacentEdges = new LinkedList<>(startingNode.getAdjacentEdges());

        Node<Vec2D> nodeToAdd;
        while(!nodes.isEmpty() && !adjacentEdges.isEmpty()) {
//            Edge<Vec2D> randomEdge = adjacentEdges.stream().findAny().get();
            Edge<Vec2D> randomEdge = adjacentEdges.get((int)(Math.random()*adjacentEdges.size()));
            /* Check if edge safe */
            nodeToAdd = null;
            if(nodes.contains(randomEdge.getNode0())) nodeToAdd = randomEdge.getNode0();
            if(nodes.contains(randomEdge.getNode1())) nodeToAdd = randomEdge.getNode1();
            if(nodeToAdd != null) {
                nodes.remove(nodeToAdd);
                adjacentEdges.addAll(nodeToAdd.getAdjacentEdges());

                // remove wall
                int dx = randomEdge.getNode1().get().x() - randomEdge.getNode0().get().x();
                int dy = randomEdge.getNode1().get().y() - randomEdge.getNode0().get().y();
                int node0X = x + randomEdge.getNode0().get().x() * 2 + 1;
                int node0Y = y + randomEdge.getNode0().get().y() * 2 + 1;
                walls[node0X][node0Y] = false;
                walls[node0X + dx][node0Y + dy] = false;
                walls[node0X + 2*dx][node0Y + 2*dy] = false;
            }

            adjacentEdges.remove(randomEdge);
        }

    }
}
