@import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode
@import java.util.Map

@param Map<ShipNavFlightMode, Long> fuel = java.util.Collections.emptyMap()
@param Map<ShipNavFlightMode, Double> duration = java.util.Collections.emptyMap()

<h4 class="title is-4">Travel <span class="subtitle is-6">(estimate)</span></h4>

<table class="table">
    <thead>
    <tr>
        <th scope="col">Mode</th>
        <th scope="col">Fuel</th>
        <th scope="col">Duration</th>
    </tr>
    </thead>
    <tbody>

    @for(var flightMode : ShipNavFlightMode.values())
        <tr>
            <td>
                ${flightMode}
            </td>
            <td>
                ${fuel.getOrDefault(flightMode, 0L)}
            </td>
            <td>
                ${duration.getOrDefault(flightMode, 0d)}
            </td>
        </tr>
    @endfor
    </tbody>
</table>
