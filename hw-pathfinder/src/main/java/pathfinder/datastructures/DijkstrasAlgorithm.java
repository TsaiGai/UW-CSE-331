package pathfinder.datastructures;

import graph.Graph;

import javax.print.attribute.standard.Finishings;
import java.util.*;

public class DijkstrasAlgorithm {
    // Dijkstra's algorithm assumes a graph with nonnegative edge weights.

    /**
     *
     * @param graph the graph that contains the path being searched
     * @param start the start node
     * @param dest the destination node
     * @param <T> generic type of node
     * @spec.requires graph != null, start != null, dest != null, active != null, start and dest are in graph
     * @return the most min-cost path from start to dest in graph or null if no such path exists
     */
    public static <T> Path<T> minPathAlgorithm(Graph<T, Double> graph, T start, T dest)
            throws IllegalArgumentException {

        // PriorityQueue<Path<T>> active = new PriorityQueue<>();
        if (graph == null || start == null || dest == null) {
            throw new IllegalArgumentException();
        } else if (!graph.listNodes().contains(start) || !graph.listNodes().contains(dest)) {
            throw new IllegalArgumentException();
        }

        // Each element is a path from start to a given node.
        // A path's 'priority' in the queue is the total cost of that path.
        // Nodes for which no path is known yet are not in the queue.
        Set<T> finished = new HashSet<>(); // set of nodes for which we know the minimum-cost path from start.

        PriorityQueue<Path<T>> active = new PriorityQueue<>((p1, p2) -> {
            if (p1.getCost() > p2.getCost()) {
                return 1;
            } else if (p1.getCost() < p2.getCost()) {
                return -1;
            }
            return 0;
        });

        // Initially we only know of the path from start to itself, which has
        // a cost of zero because it contains no edges.
        active.add(new Path<>(start));

        while (active.size() != 0) {
            // minPath is the lowest-cost path in active and,
            // if minDest isn't already 'finished,' is the
            // minimum-cost path to the node minDest
            Path<T> minPath = active.remove();
            T minDest = minPath.getEnd();

            if (minDest.equals(dest)) {
                return minPath;
            }

            if (finished.contains(minDest)) {
                continue;
            }

            for (T child : graph.listChildren(minDest).keySet()) { // For all children of minDest
                // If we don't know the minimum-cost path from start to child,
                // examine the path we've just found
                for (Double e : graph.listChildren(minDest).get(child)) {
                    if (!finished.contains(child)) {
                        Path<T> newPath = minPath.extend(child, e);
                        active.add(newPath);
                    }
                }
            }
            finished.add(minDest);
        }
        // If the loop terminates, then no path exists from start to dest.
        // The implementation should indicate this to the client.
        return null;
    }
}
