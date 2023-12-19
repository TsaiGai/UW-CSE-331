package pathfinder.junitTests;

import graph.Graph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import pathfinder.datastructures.DijkstrasAlgorithm;
import pathfinder.parser.*;

public class CampusMapTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested
    private static Graph<String, Double> graph = new Graph<>();

    /**
     * Test finding the path of a null graph.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullGraph() {
        DijkstrasAlgorithm.minPathAlgorithm(null, "node1", "node2");
    }

    /**
     * Test finding the path from a null start.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullStart() {
        DijkstrasAlgorithm.minPathAlgorithm(graph, null, "node2");
    }

    /**
     * Test finding the path to a null dest.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullDest() {
        DijkstrasAlgorithm.minPathAlgorithm(graph, "node1", null);
    }
}
