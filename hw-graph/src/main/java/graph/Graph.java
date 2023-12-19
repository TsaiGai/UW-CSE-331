package graph;

// import java.awt.*;
import java.util.*;

/**
 * Represents a mutable, finite list of nodes and edges, where each node contains a
 * list of existing edges. Each edge is unique and labeled. Each edge connects two nodes,
 * the parent and the child node, together.
 */
public class Graph <N, E> {

    public static final boolean DEBUG = false;

    /**
     * A Graph containing all the nodes and edges
     * @spec.requires name of the graph != null
     * @spec.effects this = new empty graph with name
     */

    // RI: graph != null, each parent node != null,
    // each set of edges != null, all edges in set != null
    //
    // AF: All the parent nodes are displayed in an adjacency list.
    // All the nodes of this are displayed in this.graph.keySet().
    // All the edges of a parent node are displayed in this.graph.get(parent node).
    // The children nodes are listed after each parent node. If the graph is empty, no nodes are displayed.

    private Map<N, Map<N, List<E>>> graph;
    /**
     * Creates a new, empty graph
     * @spec.effects constructs a new graph
     */
    public Graph() {
        graph = new HashMap<>();
    }

    /**
     * Adds a node to the graph
     * @param n the new node added to the graph
     * @throws IllegalArgumentException if the node being added already exists in the graph
     * @spec.requires n != null
     * @spec.modifies this
     * @spec.effects adds new node to the graph if it doesn't previously exist
     */
    public void addNode(N n) {
        checkRep();

        if (graph.containsKey(n)) {
            throw new IllegalArgumentException();
        }

        graph.put(n, new HashMap<>());
        checkRep();
    }

    /**
     * Adds an edge to connect two nodes
     * @param label name of the new edge
     * @param parent node the edge connects from
     * @param child node the edge connects to
     * @throws IllegalArgumentException if the edge being added already exists in the graph
     * @spec.requires edge != null, parent node != null, child node != null
     * @spec.modifies this
     * @spec.effects adds new edge to the graph if it doesn't previously exist
     */
    public void addEdge(N parent, N child, E label) {
        checkRep();
        if (graph.get(parent).keySet().contains(child) &&
                graph.get(parent).get(child).contains(label)) {
            throw new IllegalArgumentException();
        }

        if (graph.get(parent).keySet().contains(child)) {
            graph.get(parent).get(child).add(label);
        } else {
            List<E> edgeList = new ArrayList<>();
            edgeList.add(label);
            graph.get(parent).put(child, edgeList);
        }

        checkRep();
    }

    /**
     * Returns the nodes in the graph
     * @return a list of the nodes of the graph
     */
    public List<N> listNodes() {
        checkRep();
        return new ArrayList<>(graph.keySet());
    }

    /**
     * Returns the children of a respective parent node
     * @param parent the parent node of the graph
     * @throws IllegalArgumentException if the parent node is not in the graph
     * @spec.requires children != null
     * @return a list of the children of a parent node
     */
    public Map<N, List<E>> listChildren(N parent) {
        checkRep();
        if (!graph.containsKey(parent)) {
            throw new IllegalArgumentException();
        }

        Map<N, List<E>> result = new HashMap<>();

        for (N child : graph.get(parent).keySet()) {
            List<E> edges = new ArrayList<>(graph.get(parent).get(child));
            result.put(child, edges);
        }

        checkRep();
        return result;
    }

    /**
     * throws exceptions if the representation invariant is violated at any time
     */
    private void checkRep() {
        // Cheap tests go here
        assert (graph != null);

        if (DEBUG) {
            // Expensive tests go here
            for (N parent : graph.keySet()) {
                assert (parent != null);
                assert (graph.get(parent) != null);

                for (N child : graph.get(parent).keySet()) {
                    assert(child != null);
                    assert(graph.get(parent).get(child) != null);

                    for (E edge : graph.get(parent).get(child)) {
                        assert(edge != null);
                    }
                }
            }
        }
    }
}
