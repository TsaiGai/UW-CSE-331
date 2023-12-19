/*
 * Copyright (C) 2022 Kevin Zatloukal and James Wilcox.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Graph;
import pathfinder.datastructures.DijkstrasAlgorithm;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph<String, Double>> graphs = new HashMap<String, Graph<String, Double>>();
    private final PrintWriter output;
    private final BufferedReader input;
    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        // TODO: Implement this, reading commands from `r` and writing output to `w`.
        // See GraphTestDriver as an example.

        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        // TODO: Implement this.
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }
    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {

        graphs.put(graphName, new Graph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {

        Graph<String, Double> graph = graphs.get(graphName);
        graph.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {

        Graph<String, Double> graph = graphs.get(graphName);
        graph.addEdge(parentName, childName, Double.parseDouble(edgeLabel));
        output.println(String.format("added edge %.3f", Double.parseDouble(edgeLabel)) + " from "
                + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {

        Graph<String, Double> graph = graphs.get(graphName);
        List<String> nodes = graph.listNodes();
        StringBuilder result = new StringBuilder();
        for (String node : nodes) {
            result.append(" ");
            result.append(node);
        }
        output.println(graphName + " contains:" + result);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {

        Graph<String, Double> graph = graphs.get(graphName);
        Map<String, List<Double>> allEdges = graph.listChildren(parentName);
        StringBuilder result = new StringBuilder();
        for (String edgeList : allEdges.keySet()) {
            List<Double> edges = allEdges.get(edgeList);
            for (Double edge : edges) {
                result.append(" ").append(edgeList);
                result.append("(").append(String.format("%.3f", edge)).append(")");
            }
        }
        output.println("the children of " + parentName + " in " + graphName + " are:" + result);
    }

    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName1 = arguments.get(1);
        String nodeName2 = arguments.get(2);
        findPath(graphName, nodeName1, nodeName2);
    }

    private void findPath(String graphName, String nodeName1, String nodeName2) {
        Graph<String, Double> graph = graphs.get(graphName);
        boolean test = true;

        try {
            DijkstrasAlgorithm.minPathAlgorithm(graph, nodeName1, nodeName1);
        } catch (IllegalArgumentException e) {
            output.println("unknown: " + nodeName1);
            test = false;
        }

        try {
            DijkstrasAlgorithm.minPathAlgorithm(graph, nodeName2, nodeName2);
        } catch (IllegalArgumentException e) {
            output.println("unknown: " + nodeName2);
            test = false;
        }

        if (test) {
            output.println("path from " + nodeName1 + " to " + nodeName2 + ":");
            Path<String> path = DijkstrasAlgorithm.minPathAlgorithm(graph, nodeName1, nodeName2);
            if (path == null) {
                output.println("no path found");
                test = false;
            } else {
                Iterator<Path<String>.Segment> itr = path.iterator();
                while (itr.hasNext()) {
                    Path<String>.Segment segment = itr.next();
                    StringBuilder result = new StringBuilder();
                    result.append(segment.getStart() + " to " + segment.getEnd() + " with weight ");
                    result.append(String.format("%.3f", segment.getCost()));
                    output.println(result);
                }
                output.println(String.format("total cost: %.3f", path.getCost()));
            }
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
