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
// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import Map from "./Map";

import {MLine} from "./Interfaces";
import BuildingList from "./BuildingList";

// constructs an interface that contains the data inputted by the user
interface AppState {
    mLines: MLine[]
}

class App extends Component<{}, AppState> {
    constructor(props: any) {
        super(props);
        this.state = {
            mLines: []
        };
    }

    // method that sends a request to the server for the shortest path
    handleChange = (mLines : MLine[]) => {
        this.setState({
            mLines : mLines
        })
    }

    // method that clears the contents of the map and user input
    clearMap = () => {
        this.setState({
            mLines: []
        })
    }

    render() {
        return (
            <div>
                <h1 id={"app-title"}>
                    Find the Shortest Path!
                </h1>
                <div>
                    {/* defines props in the map */}
                    <Map mLines={this.state.mLines}/>
                </div>
                {/* stores user input in the state */}
                <BuildingList
                    onChange={this.handleChange}
                    onClear={this.clearMap}/>
            </div>
        );
    }
}

export default App;
