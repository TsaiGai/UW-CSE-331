package graph.junitTests;

import org.junit.Test;
import static org.junit.Assert.*;
import graph.Graph;

import org.junit.Rule;
import org.junit.rules.Timeout;

/**
 * GraphTest is a test file for the Graph class
 */

public class GraphTest {

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested
    private static Graph<String, String> graph1= new Graph<>();
    private static Graph<String, String> graph2= new Graph<>();

    /**
     * Test adding the same node in a graph.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testAddSameNode() {
        graph1.addNode("node");
        graph1.addNode("node");
    }

    /**
     * Test adding the same edge in a graph.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testAddSameEdge() {
        graph1.addNode("node1");
        graph1.addNode("node2");
        graph1.addEdge("node1", "node2", "edge");
        graph1.addEdge("node1", "node2", "edge");
    }

    /**
     * Test adding an edge to a node that already has an edge in a graph.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testAddEdgeTwice() {
        graph1.addNode("node1");
        graph1.addNode("node2");
        graph1.addEdge("edge", "node1", "node2");
        graph1.addEdge("edge2", "node1", "node2");
    }

    /**
     * Test adding an edge to a non-existent parent.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEdgeToNoParent() {
        graph1.addNode("node2");
        graph1.addEdge("edge", "node1", "node2");
    }

    /**
     * Test adding an edge to a non-existent child.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEdgeToNoChild() {
        graph1.addNode("node1");
        graph1.addEdge("edge", "node1", "node2");
    }

    /**
     * Test printing children of non-existent node.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testListChildrenNoNode() {
        graph2.listChildren("node1");
    }
}
