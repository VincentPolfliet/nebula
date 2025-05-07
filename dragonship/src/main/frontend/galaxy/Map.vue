<script setup lang="ts">
import {isInOrbit, isWaypointShipType, Waypoint, WaypointType, WaypointTypeFilter} from "./map.models";
import {onMounted, ref} from "vue";
import {
    canvas,
    CRS,
    divIcon,
    featureGroup,
    FeatureGroup,
    latLng,
    map,
    Map,
    MapOptions,
    Marker,
    marker, svg,
    Util
} from "leaflet";
import 'leaflet/dist/leaflet.css';
import {newRng} from "../utils/rng";
import {asPoint, drawGalaxyRings, Point} from "./galaxy.utils";
import indexOf = Util.indexOf;

const props = defineProps({
    waypoints: Array<Waypoint>,
    types: Array<WaypointType>,
    target: String,
    seed: String
})

const ORBIT_RADIUS_PX = 10;
const SHIP_ORBIT_RADIUS_PX = 1.5;
const rng = newRng(props.seed);

const waypointTypes = ref([...props.types].map(w => {
    return {
        "title": w.title,
        "type": w.type,
        "menuActive": false,
        "filterActive": true,
        "iconClass": w.type.toLowerCase(),
    } as WaypointTypeFilter
}));

const getWaypointsByType = function (type: string) {
    return props.waypoints
        .filter(w => w.type === type);
}

const getWaypointsByOrbitsAround = function (orbits: string | undefined) {
    if (!orbits) {
        return [];
    }

    return props.waypoints
        .filter(w => w.orbits === orbits);
}

const onTypeClicked = function (item: WaypointTypeFilter) {
    item.menuActive = !item.menuActive;
}

const navigateToWaypoint = function (symbol: string) {
    const marker = markersPerWaypoint[symbol];

    if (marker) {
        galaxyMap.flyTo(marker.getLatLng(), 5);
        marker.openPopup();
    }
}

function calculateXYonMap(waypoint: Waypoint, siblings: Waypoint[]): Point {
    if (!isInOrbit(waypoint)) {
        return {x: waypoint.x, y: waypoint.y};
    }

    const index = indexOf(siblings, waypoint);
    const angle = (index + rng() * 0.2) / siblings.length * 2 * Math.PI;

    const isShipType = isWaypointShipType(waypoint);
    const orbitRadius = isShipType ? SHIP_ORBIT_RADIUS_PX : ORBIT_RADIUS_PX;
    const offsetX = waypoint.x + Math.cos(angle) * orbitRadius;
    const offsetY = waypoint.y + Math.sin(angle) * orbitRadius;

    return {x: offsetX, y: offsetY};
}

function resetAllFilters(): void {
    for (const waypointType of waypointTypes.value) {
        waypointType.filterActive = true;
    }

    fillMarkers();
}

function fillMarkers() {
    if (markersFeatureGroup) {
        galaxyMap.removeLayer(markersFeatureGroup);
    }

    markersPerWaypoint = [];
    const markers = [];

    for (const waypointType of waypointTypes.value) {
        if (waypointType.filterActive) {
            for (const waypoint of getWaypointsByType(waypointType.type)) {

                const {y, x} = calculateXYonMap(waypoint, getWaypointsByOrbitsAround(waypoint.orbits));

                // using priority as alt to order markers
                const waypointLatLng = latLng([y, x, waypoint.priority]);

                const waypointMarker: Marker = marker(waypointLatLng, {
                    icon: divIcon({
                        className: `system ${waypoint.type.toLowerCase()}`,
                        html: '<span></span>'
                    })
                });

                waypointMarker.bindPopup(`<p>${waypoint.symbol} [${waypoint.x}, ${waypoint.y}]</p>`);

                markers.push(waypointMarker);
                markersPerWaypoint[waypoint.symbol] = waypointMarker;
            }
        }
    }

    if (markers.length !== 0) {
        markersFeatureGroup = featureGroup(markers);
        markersFeatureGroup.addTo(galaxyMap);
    }
}

let galaxyMap: Map;
let markersFeatureGroup: FeatureGroup
let markersPerWaypoint = []

onMounted(() => {
    const renderer = /firefox/i.test(navigator.userAgent) ? canvas() : /chrom(e|ium)/i.test(navigator.userAgent) ? svg() : canvas(); // use canvas on firefox, svg on chrome

    const options: MapOptions = {
        crs: CRS.Simple,
        renderer: renderer,
    };

    galaxyMap = map('galaxy-map', options);
    fillMarkers();
    drawGalaxyRings(galaxyMap, props.waypoints.map(asPoint), {
        color: "white"
    });

    if (markersFeatureGroup) {
        galaxyMap.fitBounds(markersFeatureGroup.getBounds());

        if (props.target) {
            navigateToWaypoint(props.target)
        }
    }
})
</script>

<template>
    <div class="columns">
        <div class="column is-one-fifth galaxy-map__column">
            <aside class="menu galaxy-map__controls">
                <p class="menu-label" @click="resetAllFilters()">Legend</p>
                <ul class="menu-list">
                    <li v-for="type in waypointTypes">
                        <a class="level-item" @click="onTypeClicked(type)">
                            <span> <span :class="type.iconClass"></span> {{ type.title }}</span>
                        </a>

                        <ul v-bind:class="{ 'is-hidden': !type.menuActive }">
                            <li v-for="waypoint in getWaypointsByType(type.type)"
                                @click="navigateToWaypoint(waypoint.symbol)">
                                <a>{{ waypoint.symbol }}</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </aside>
        </div>
        <div class="column galaxy-map__column">
            <div id="galaxy-container" class="galaxy-map__container">
                <div id="galaxy-map" class="galaxy-map__map">

                </div>
            </div>
        </div>
    </div>
</template>

<style>
.leaflet-container {
    background: var(--space-black);
}

.galaxy-map__container {
    min-height: 100%;
    max-width: 100%;
    height: 100%;
    width: 100%;
}

.galaxy-map__map {
    height: calc(100vh - var(--header-height));
    min-height: 100%;
    min-width: 100%;
    display: block;
}

.galaxy-map__controls {
    padding: 0.75em;
    overflow: auto;
}

.galaxy-map__column {
    padding-bottom: 0;
}

.galaxy-map__filter-control {
    background: var(--bulma-background);
}

.system {
    width: 100%;
    text-align: center;
    font-size: 2em;
}

.asteroid::after, .engineered_asteroid::after, .asteroid_base::after {
    content: "☄️";
}

.asteroid_base {
    color: transparent;
    text-shadow: 0 0 0 mediumvioletred;
}

.engineered_asteroid {
    color: transparent;
    text-shadow: 0 0 0 cornflowerblue;
}

.planet::after {
    content: "🪐";
}

.moon::after {
    content: "🌕";
}

.gas_giant::after {
    content: "🌠";
}

.fuel_station::after {
    content: "⛽";
}

.orbital_station::after {
    content: "🛰️";
}

.jump_gate::after {
    content: "✈️";
}

.ship::after {
    content: "🚀";
}

</style>
