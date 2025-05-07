import L from 'leaflet';
import {Waypoint} from "./map.models";

export type Point = { x: number; y: number };

export function asPoint(waypoint: Waypoint): Point {
    return {x: waypoint.x, y: waypoint.y};
}

/**
 * Draw "galaxy" rings around the centroid of waypoint objects.
 * Determines ring count dynamically and positions rings at waypoints' distribution quantiles,
 * so circles align closely with your data clusters.
 *
 * @param map       Leaflet map instance (e.g., CRS.Simple)
 * @param waypoints Array of objects with {x, y} in map CRS
 * @param options   Optional Leaflet circle style overrides (e.g., color)
 */
export function drawGalaxyRings(
    map: L.Map,
    waypoints: Point[],
    options?: { color?: string }
): void {
    if (!waypoints || waypoints.length === 0) {
        console.warn('drawGalaxyRings: no waypoints provided → skipping.');
        return;
    }

    // Compute centroid
    const centroid: Point = waypoints.reduce(
        (acc, pt, _, arr) => ({x: acc.x + pt.x / arr.length, y: acc.y + pt.y / arr.length}),
        {x: 0, y: 0}
    );

    // Distances from centroid
    const distances = waypoints.map(pt => Math.hypot(pt.x - centroid.x, pt.y - centroid.y));
    const sorted = [...distances].sort((a, b) => a - b);
    const n = sorted.length;

    // Compute IQR for dynamic bin width
    const q1 = sorted[Math.floor(n * 0.25)];
    const q3 = sorted[Math.floor(n * 0.75)];
    const iqr = q3 - q1;
    const binWidth = iqr > 0 ? (2 * iqr) / Math.pow(n, 1 / 3) : sorted[n - 1] / 4;

    // Determine ring count, clamp between 2 and 20
    let ringCount = binWidth > 0
        ? Math.ceil((sorted[n - 1] - sorted[0]) / binWidth)
        : 4;
    ringCount = Math.min(Math.max(ringCount, 2), 20);

    // Generate radii at quantiles to align with waypoint clusters
    const radii = Array.from({length: ringCount}, (_, i) => {
        const idx = Math.min(n - 1, Math.floor(((i + 1) * n) / (ringCount + 1)));
        return sorted[idx];
    });

    // Convert centroid to LatLng
    const center = map.unproject(L.point(centroid.x, centroid.y));

    // Draw circles
    radii.forEach((radius, i) => {
        L.circle(center, {
            radius,
            weight: 1,
            fill: false,
            color: `hsl(${(i / ringCount) * 360}, 50%, 70%)`,
            ...options,
        }).addTo(map);
    });
}

