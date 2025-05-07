export interface Waypoint {
    symbol: string
    type: string
    x: number
    y: number
    priority: number
    orbits: string | undefined
}

export function isWaypointShipType(waypoint: Waypoint) {
    return waypoint.type === 'SHIP';
}

export function isInOrbit(waypoint: Waypoint) {
    return waypoint.orbits !== null;
}

export interface WaypointType {
    title: string,
    type: string
}

export interface WaypointTypeFilter {
    title: string,
    type: string,
    filterActive: boolean
    menuActive: boolean
    iconClass: string
}
