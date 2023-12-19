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
import {MLine} from "./MLine";

interface EdgeListProps {
    onChange(edges: Array<MLine>): void;  // called when a new edge list is ready
                                 // TODO: once you decide how you want to communicate the edges to the App, you should
                                 // change the type of edges so it isn't `any`
    onClear(): void;
}

interface EdgeListState {
    textValue: any
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeListState> {
    constructor(props: any) {
        super(props);
        this.state = {
            textValue: ""
        }
    }

    edgeTextChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        this.setState({
            textValue: e.target.value
        })
    }

    callOnClear = () => {
        this.props.onClear();
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={this.edgeTextChange}
                    value={this.state.textValue}
                /> <br/>
                <button onClick={() => {
                    let edges: string[][] = []
                    let mLineArray: Array<MLine> = []

                    // puts each piece of the text input into an array
                    this.state.textValue.split("\n")
                        .forEach((element: string) => edges.push(element.trim().split(/\s+/)));

                    for (let i = 0; i < edges.length; i++) {
                        let valid: boolean = true;
                        let edgeString: any = edges[i].toString()

                        // eliminates any whitespace and unnecessary formatting in the input
                        let array = edgeString.toString().replace(/\r?\n/, ' ')
                            .trim().replace(/\s+/g, ' ').replace(/,/g, ' ')
                            .split(' ');
                        let edgeArray: Array<number> = []

                        for (let j = 0; j < 4; j++) {
                            if (!(parseFloat(array[j]) > 4000 || parseFloat(array[i]) < 0
                                || isNaN(parseFloat(array[j])))) {
                                edgeArray.push(array[j]);
                            } else {
                                alert("Please provide a valid input (must be in the form [x1 x2 y1 y2 color] " +
                                    "and 0 <= coordinates <= 4000)")
                                valid = false;
                                break;
                            }
                        }

                        if (valid) {
                            for (let i = 0; i < 4; i++) {
                                const newLine: MLine = {
                                    x1: edgeArray[0],
                                    x2: edgeArray[1],
                                    y1: edgeArray[2],
                                    y2: edgeArray[3],
                                    color: array[4]
                                }

                                mLineArray.push(newLine)
                            }

                            this.props.onChange(mLineArray)
                        }
                    }
                }}>Draw</button>
                <button onClick={() => {
                    this.setState({textValue: ""})
                    this.callOnClear()
                }}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
