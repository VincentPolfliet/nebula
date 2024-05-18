@import dev.vinpol.spacetraders.sdk.models.*
@import dev.vinpol.spacetraders.sdk.models.Waypoint
@import dev.vinpol.spacetraders.sdk.models.WaypointTrait
@import dev.vinpol.spacetraders.sdk.models.WaypointModifier
@import java.util.Map

@param Ship ship
@param Waypoint target
@param double distance
@param ShipNavFlightMode suggested = dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode.CRUISE /* TODO calculate suggested using the mins of fuel and duration*/
@param Map<ShipNavFlightMode, Long> fuel
@param Map<ShipNavFlightMode, Double> duration

<div class="tags">
    @for(WaypointTrait traitSymbol : target.getTraits())
        <span class="tag">${traitSymbol.getSymbol().name()}</span>
    @endfor
    @for(WaypointModifier modifierSymbol : target.getModifiers())
        <span class="tag">${modifierSymbol.getSymbol().name()}</span>
    @endfor
</div>

<div class="columns">
    <div class="column">
        <h4 class="title is-4">Travel <span class="subtitle is-6">(estimate)</span></h4>

        <table class="table">
            <thead>
            <tr>
                <th scope="col">Mode</th>
                <th scope="col">Duration</th>
                <th scope="col">Cost</th>
                <th scope="col">Remaining</th>
            </tr>
            </thead>
            <tbody>


            @for(var flightMode : ShipNavFlightMode.values())
                !{var styleClass = flightMode == suggested ? "has-text-success" : "";}
                <tr class="${styleClass}">
                    <td class="${styleClass}">
                        ${flightMode}
                    </td>
                    <td class="${styleClass}">
                        ${duration.getOrDefault(flightMode, 0d)}
                    </td>
                    <td class="${styleClass}">
                        !{var fuelCost = fuel.getOrDefault(flightMode, 0L);}
                        ${fuelCost}
                    </td>

                    !{var remainingFuel = ship.getFuel().getCurrent() - fuelCost;}
                    @if(flightMode != suggested)
                        <td class="${remainingFuel < 0 ? "has-text-danger" : "" }">
                            ${remainingFuel}
                        </td>
                    @else
                        <td class="${styleClass}">
                            ${remainingFuel}
                        </td>
                    @endif
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
</div>
