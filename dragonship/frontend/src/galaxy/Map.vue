<script setup>
import {onMounted, ref} from "vue";

const props = defineProps({
    waypoints: Array,
    types: Array,
    target: String
})

const waypointTypes = ref([...props.types].map(w => {
    return {
        "title": w.title,
        "type": w.type,
        "menuActive": false,
        "filterActive": true,
        "iconClass": w.type.toLowerCase(),
    }
}));

const getWaypointsByType = function (type) {
    return props.waypoints
        .filter(w => w.type === type);
}

const onFilterClicked = function (item) {
    fillMarkers();
}

const onTypeClicked = function (item) {
    item.menuActive = !item.menuActive;
}

const navigateToWaypoint = function (symbol) {
    const marker = markersPerWaypoint[symbol];

    if (marker) {
        map.flyTo(marker.getLatLng(), 5);
        marker.openPopup()
    }
}

function resetAllFilters() {
    for (const waypointType of waypointTypes.value) {
        waypointType.filterActive = true;
    }

    fillMarkers();
}

let map;
let markersFeatureGroup
let markersPerWaypoint = []

onMounted(() => {
    map = L.map('galaxy-map', {
        crs: L.CRS.Simple
    });

    fillMarkers();

    if (markersFeatureGroup) {
        map.fitBounds(markersFeatureGroup.getBounds());

        if (props.target) {
            navigateToWaypoint(props.target)
        }
    }
})

function fillMarkers() {
    if (markersFeatureGroup) {
        map.removeLayer(markersFeatureGroup);
    }

    markersPerWaypoint = []
    const markers = [];

    for (const waypointType of waypointTypes.value) {
        if (waypointType.filterActive) {
            for (const waypoint of getWaypointsByType(waypointType.type)) {
                // using priority as alt to order markers
                // this looks to work, but I have no idea if it actually does
                const waypointLatLng = L.latLng([waypoint.y, waypoint.x, waypoint.priority]);

                const marker = L.marker(waypointLatLng, {
                    icon: L.divIcon({
                        className: `system ${waypoint.type.toLowerCase()}`,
                        html: '<span></span>'
                    })
                });

                const isShipType = waypointType.type === 'SHIP';
                const displayX = isShipType ? Math.floor(waypoint.x) : waypoint.x;
                const displayY = isShipType ? Math.floor(waypoint.y) : waypoint.y;

                marker.bindPopup(`<p>${waypoint.symbol} [${displayX}, ${displayY}]</p>`);

                markers.push(marker);
                markersPerWaypoint[waypoint.symbol] = marker;
            }
        }
    }

    if (markers.length !== 0) {
        markersFeatureGroup = L.featureGroup(markers);
        markersFeatureGroup.addTo(map);
    }
}

</script>

<template>
    <div class="columns">
        <div class="column is-one-fifth galaxy-map__column">
            <aside class="menu galaxy-map__controls">
                <p class="menu-label" @click="resetAllFilters()">Legend</p>
                <ul class="menu-list">
                    <li v-for="type in waypointTypes">
                        <!-- TODO: fix this not taking the entire bar -->
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
