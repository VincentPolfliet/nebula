@import dev.vinpol.nebula.dragonship.web.fleet.FleetController
@import dev.vinpol.nebula.dragonship.web.galaxy.TravelCostEstimate
@import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode
@import java.util.List
@import static dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer.i18n

@param List<ShipNavFlightMode> keys = java.util.Arrays.<dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode>asList(dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode.values())
@param List<TravelCostEstimate> costs = java.util.Collections.<dev.vinpol.nebula.dragonship.web.galaxy.TravelCostEstimate>emptyList()

@if(costs.isEmpty())
    <div class="notification">
        <p>${i18n("fleet.navigate.placeholder")}</p>
    </div>
@else
    <table class="table is-fullwidth">
        <thead>
        <tr>
            <th scope="col">Mode</th>
            <th scope="col">Duration</th>
            <th scope="col">Cost</th>
            <th scope="col">Remaining</th>
        </tr>
        </thead>
        <tbody>
        @for(var flightMode : keys)
            !{var cost = FleetController.findCostByMode(costs, flightMode).orElse(null);}

            !{var styleClass = (cost != null && cost.isSuggested() ? "has-text-success" : "");}
            <tr>
                <td class="${styleClass}">
                    ${flightMode}
                </td>

                <td class="${styleClass} is-number">
                    @if(cost != null)
                        !{var durationCost = cost.duration();}
                        @if(durationCost.toMinutes() < 1)
                            ${durationCost.toSeconds()} s
                        @else
                            <time class="humanize humanize-relativetime" data-humanize-units="second"
                                  datetime="${durationCost.toString()}">${durationCost.toSeconds()}</time>
                        @endif
                    @endif
                </td>

                <td class="${styleClass} is-number">
                    @if(cost != null)
                        !{var fuelCost = cost.fuelCost();}
                        ${fuelCost}
                    @endif
                </td>

                @if(cost != null)
                    !{var remainingFuelCost = cost.remainingFuel();}
                    @if(cost.hasRemainingFuel())
                        <td class="${remainingFuelCost < 0 ? "has-text-danger" : cost.isSuggested() ? styleClass : "" } is-number">
                            ${remainingFuelCost}
                        </td>
                    @else
                        <td class="${styleClass} is-number">
                            &#x221e;
                        </td>
                    @endif
                @else
                    <td></td>
                @endif
            </tr>
        @endfor
        </tbody>
    </table>
@endif
