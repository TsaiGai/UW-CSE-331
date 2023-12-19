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

import React, {Component} from 'react';
import {MLine, MPath} from "./Interfaces";

// defines a default text value for the two drop down menus
const START_DEFAULT_VALUE = "Please select a building to start from"
const END_DEFAULT_VALUE = "Please select a building to end on"

interface BuildingListProps {
    onChange(edges: MLine[]): void;  // called when a new edge list is ready
    // TODO: once you decide how you want to communicate the edges to the App, you should
    // change the type of edges so it isn't `any`
    onClear(): void;
}

// constructs an interface for the buildings in the map
interface Building {
    index : string;
    name : string;
}

// constructs an interface for the list of buildings in the map
interface BuildingListState {
    buildings : Building[];
    shortNameList : Set<string>;
    start: string
    end: string
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class BuildingList extends Component<BuildingListProps, BuildingListState> {
    constructor(props: any) {
        super(props);
        this.state = {
            buildings: [],
            shortNameList: new Set(),
            start: START_DEFAULT_VALUE,
            end: END_DEFAULT_VALUE
        }
    }

    // method that provides a selection of buildings
    selection = (evt: any, buildingType: string) => {
        // the index of the selected building
        let building = evt.target.value;

        // updates the state based on the drop-down menu selected
        if (buildingType === "start") {
            this.setState({
                start: building
            });
        } else if (buildingType === "end") {
            this.setState({
                end: building
            });
        }
    }

    // method that gets the buildings from the server
    async componentDidMount() {
        try {
            let responsePromise = await fetch("http://localhost:4567/campus-buildings");
            if (!responsePromise.ok) {
                alert("not ok");
                return;
            }

            // constructs variables for the list of buildings
            let response : string[] = JSON.parse(await responsePromise.text());
            let buildings : any = [];
            let shortNameList : Set<string> = new Set();

            // adds the buildings to a set of strings
            for (let building in response) {
                buildings.push({index:building, name:response[building]});
                shortNameList.add(response[building]);
            }

            // sets the state
            this.setState({
                buildings : buildings,
                shortNameList : shortNameList
            })

            return buildings;

        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    // method that requests the server to find the shortest path
    findPath = async ()  => {
        // sets the start and end buildings to the name when chosen by the user
        let start : string = this.state.start
        for (let element of this.state.buildings) {
            if (element.index === start) {
                start = element.name;
            }
        }
        let end : string = this.state.end
        for (let element of this.state.buildings) {
            if (element.index === end) {
                end = element.name;
            }
        }

        try {
            let responsePromise = await fetch("http://localhost:4567/campus-path?start=" + start + "&end=" + end);
            let response = await responsePromise;
            if (!response.ok) {
                alert(`Error retrieving shortest path: ${response.statusText}`);
            }

            // converts the data from the server to a readable string and MPath
            let rawData : any = (await response.text()).toString();
            let string = JSON.parse(rawData)
            let data : MPath = string

            // makes a call to the map to add the path
            this.props.onChange(data.path);

        } catch (e) {
            alert("There was an error in loading the path, this issue will be fixed shortly :)");
            console.log(e);
        }
    }

    // method that clears the contents of the site
    callOnClear = () => {
        this.props.onClear();
    }

    render() {
        // generate the options for the drop-down menu from the list of buildings
        let options : any = [];
        for (let building of this.state.buildings) {
            options.push(<option value={building.index}>{building.name}</option>);
        }

        return (
            <div id="building-list">
                {/* constructs two drop-down menus for the start and end buildings */}
                <select onChange={(evt) => this.selection(evt, "start")} value={this.state.start}>
                    <option value={START_DEFAULT_VALUE}>{START_DEFAULT_VALUE}</option>
                    {options}
                </select>
                <select onChange={(evt) => this.selection(evt, "end")} value={this.state.end}>
                    <option value={END_DEFAULT_VALUE}>{END_DEFAULT_VALUE}</option>
                    {options}
                </select>
                {/* button that draws the shortest path between start and end */}
                <button onClick={() => {
                    let start : string = this.state.start
                    let end : string = this.state.end
                    this.findPath()
                }}>Draw</button>
                {/* button that clears the contents of the site */}
                <button onClick={() => {
                    this.setState({
                        start : START_DEFAULT_VALUE,
                        end : END_DEFAULT_VALUE
                    })
                    this.callOnClear()
                }}>Clear</button>
            </div>
        );
    }
}

export default BuildingList;
