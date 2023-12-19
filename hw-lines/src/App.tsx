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

import React, {Component} from "react";
import EdgeList from "./EdgeList";
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import {MLine} from "./MLine";

interface AppState {
    mLines: Array<MLine>
}

class App extends Component<{}, AppState> { // <- {} means no props.

    constructor(props: any) {
        super(props);
        this.state = {
            // TODO: store edges in this state
            mLines:[]
        };
    }

    storeEdges = (edges: Array<MLine>) => {
        let newLine: MLine[] = [];

        for (let i = 0; i < edges.length; i++ ) {
            let x1: number = edges[i].x1
            let x2: number = edges[i].x2
            let y1: number = edges[i].y1
            let y2: number = edges[i].y2
            let color: string = edges[i].color

            newLine.push({x1: x1, x2: x2, y1: y1, y2: y2, color: color})
        }

        this.setState({
            mLines: newLine
        })
    }

    clearMap = () => {
        this.setState({
            mLines: []
        })
    }

    render() {
        return (
            <div>
                <h1 id="app-title">Line Mapper!</h1>
                <div>
                    {/* TODO: define props in the Map component and pass them in here */}
                    <Map mLines={this.state.mLines}/>
                </div>
                <EdgeList
                    onChange={
                        // TODO: Modify this onChange callback to store the edges in the state
                        this.storeEdges
                    }
                    onClear={this.clearMap}/>
            </div>
        );
    }
}

export default App;
