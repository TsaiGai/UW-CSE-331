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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import spark.Route;
import spark.Spark;

import java.util.*;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        // TODO: Create all the Spark Java routes you need here.
        // constructs a new Gson and CampusMap for the server
        Gson gson = new Gson();
        CampusMap cMap = new CampusMap();

        // this route contains all the building names on the map
        Spark.get("/campus-buildings", (request, response) -> {
            // constructs a map and list for the short and long names to be added in
            Map<String, String> names = cMap.buildingNames();
            List<String> list = new ArrayList<>(names.keySet());

            // constructs a Gson of the short and long names and returns it to the view
            Gson nameList = new Gson();
            return nameList.toJson(list.toArray());
        });

        // this route contains the shortest path between a start and end building
        Spark.get("/campus-path", (request, response) -> {
            String start = request.queryParams("start");
            String end = request.queryParams("end");

            return gson.toJson(cMap.findShortestPath(start, end));
        });
    }
}
