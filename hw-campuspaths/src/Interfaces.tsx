export interface Coordinates {
    x : number
    y : number
}

// creates an interface to hold edge objects
export interface MLine {
    start : Coordinates
    end : Coordinates
    cost : number
}

export interface MPath {
    start : Coordinates
    path : MLine[]
    cost : number
}