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

package pathfinder;

import pathfinder.datastructures.DijkstrasAlgorithm;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;

import graph.Graph;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;

/**
 * Represents an immutable map that connects the buildings of the UW campus their respective names.
 */
public class CampusMap implements ModelAPI {

    public static final boolean DEBUG = false;

    // RI: graph != null, nameMap != null,
    // every key and its mapped element != null
    //
    // AF: All the short names of a map are displayed in nameMap.keySet().
    // All the long names of a corresponding short name in a map are displayed in nameMap.get(shortName).getLongName().
    // All the coordinates of a building in a map are displayed in nameMap.get(shortName).getX()
    // and nameMap.get(shortName).getY().
    // All the nodes of a map are displayed in graph.keySet().
    // All the edges of a parent node are displayed in graph.get(parent node).

    private final Graph<Point, Double> graph;
    private final Map<String, CampusBuilding> nameMap;

    /**
     * Creates a new campus map
     * @spec.effects constructs a new campus map
     */
    public CampusMap() {
        graph = new Graph<>();
        nameMap = new HashMap<>();

        List<CampusBuilding> buildingList = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        for (CampusBuilding building : buildingList) {
            nameMap.put(building.getShortName(), building);
        }

        List<CampusPath> pathList = CampusPathsParser.parseCampusPaths("campus_paths.csv");
        for (CampusPath path : pathList) {
            try {
                graph.addNode(new Point(path.getX1(), path.getY1()));
                graph.addNode(new Point(path.getX2(), path.getY2()));
                graph.addEdge(new Point(path.getX1(), path.getY1()), new Point(path.getX2(), path.getY2()),
                        path.getDistance());
            } catch (IllegalArgumentException ignored){}
        }
    }
    /**
     * Determines if the short name of a building exists
     * @param shortName The short name of a building to query.
     * @return {@literal true} iff the short name provided exists in this campus map.
     */
    @Override
    public boolean shortNameExists(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI

        checkRep();
        return nameMap.containsKey(shortName);
    }

    /**
     * Finds the long name for a given short name
     * @param shortName The short name of a building to look up.
     * @return The long name of the building corresponding to the provided short name.
     * @throws IllegalArgumentException if the short name provided does not exist.
     */
    @Override
    public String longNameForShort(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI

        if (!shortNameExists(shortName)) {
            throw new IllegalArgumentException();
        }

        checkRep();
        return nameMap.get(shortName).getLongName();
    }

    /**
     * Returns the names of the buildings in the map
     * @return A mapping from all the buildings' short names to their long names in this campus map.
     */
    @Override
    public Map<String, String> buildingNames() {
        // TODO: Implement this method exactly as it is specified in ModelAPI

        checkRep();
        Map<String, String> buildingMap = new HashMap<>();

        for (String name : nameMap.keySet()) {
            buildingMap.put(name, nameMap.get(name).getLongName());
        }

        checkRep();
        return buildingMap;
    }

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
     * if none exists.
     * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
     *                                  {@literal null}, or not valid short names of buildings in
     *                                  this campus map.
     */
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI

        if (startShortName == null || endShortName == null) {
            throw new IllegalArgumentException();
        } else if (!shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException();
        }

        checkRep();
        Point startPoint = new Point(nameMap.get(startShortName).getX(), nameMap.get(startShortName).getY());
        Point endPoint = new Point(nameMap.get(endShortName).getX(), nameMap.get(endShortName).getY());

        return DijkstrasAlgorithm.minPathAlgorithm(graph, startPoint, endPoint);

    }

    private void checkRep() {
        // Cheap tests go here
        assert (graph != null);
        assert (nameMap != null);

        if (DEBUG) {
            // Expensive tests go here
            for (String shortName : nameMap.keySet()) {
                assert (shortName != null);
                assert (nameMap.get(shortName) != null);
            }
        }
    }
}
