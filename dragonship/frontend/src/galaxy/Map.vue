<script setup>
import {computed, h, onMounted, onUpdated, ref, render} from "vue";
import FilterPanel from "./FilterPanel.vue";

const props = defineProps({
    waypoints: Array
})

const innerWaypointTypes = [...new Set(props.waypoints.map(w => w.type))].map(w => {
    return {
        "active": true,
        "iconClass": w.toLowerCase(),
        "title": w
    }
});

const waypointTypes = ref(innerWaypointTypes)

let map;
let markersFeatureGroup

onMounted(() => {
    function makeFilterPanel(element, data) {
        const vueComponent = h(FilterPanel, data);

        render(vueComponent, element)
    }

    map = L.map('galaxy-map', {
        crs: L.CRS.Simple
    });

    L.Control.Filter = L.Control.extend({
        options: {
            position: 'topright'
        },
        onAdd: function (map) {
            const container = document.createElement("div");
            container.classList.add("leaflet-control");

            // https://vuejs.org/guide/extras/render-function
            // TODO: event listener or a way to have a callback so that we can render the markers again		
	    // https://stackoverflow.com/questions/66781022/how-to-add-listener-in-setup-render-function-to-event-updatemyprop ???
            makeFilterPanel(container, {types: waypointTypes.value});
            return container;
        },
        onRemove: function (map) {
        },
    });

    const filter = new L.Control.Filter()
    filter.addTo(map);

    if (markersFeatureGroup) {
        map.removeLayer(markersFeatureGroup);
    }

    const markers = [];

    for (const waypoint of props.waypoints) {
        const waypoinLatLng = L.latLng([waypoint.y, waypoint.x]);

        const marker = L.marker(waypoinLatLng, {
            icon: L.divIcon({
                className: `system ${waypoint.type.toLowerCase()}`,
                html: '<span></span>'
            })
        }).bindPopup(`<p>${waypoint.symbol} (${waypoint.type}) </p>`);

        markers.push(marker);
    }

    markersFeatureGroup = L.featureGroup(markers);
    markersFeatureGroup.addTo(map);

    map.fitBounds(markersFeatureGroup.getBounds());
})

onUpdated(() => {
    console.log("map updated")
})

</script>

<template>
    <div id="galaxy-container" class="galaxy-map__container">
        <div id="galaxy-map" class="galaxy-map__map">

        </div>
    </div>
</template>

<style>
:root {
    --space-black: rgb(22, 23, 81);
    --header-height: var(--bulma-navbar-height);
}

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

.galaxy-map__filter-control {
    background: var(--bulma-background);
}

.system {
    width: 100%;
    text-align: center;
    font-size: 2em;
}

.asteroid::after {
    content: "☄️";
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
    content: "🚀";
}

</style>
