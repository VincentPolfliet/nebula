@import dev.vinpol.nebula.dragonship.automation.ActivityState
@import dev.vinpol.nebula.dragonship.utils.time.TimeWizard
@import dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer
@import dev.vinpol.spacetraders.sdk.models.*
@import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode
@import dev.vinpol.spacetraders.sdk.models.ShipNavStatus
@import dev.vinpol.spacetraders.sdk.models.WaypointType

@import java.util.Set
@import static dev.vinpol.nebula.dragonship.web.fleet.FleetUtils.*

@param Ship ship
@param Set<ShipRole> automationSupport
@param TimeWizard clock

<tr id="ship-row-${ship.getSymbol()}">
    <td><a href="/ship/detail/${ship.getSymbol()}">${ship.getSymbol()}</a></td>

    <td>${ship.getRole()}</td>

    <td class="is-number">
        ${String.format("%.0f", calculateAverageCondition(ship))}
    </td>

    <td class="is-number">
        ${String.format("%.0f", calculateAverageIntegrity(ship))}
    </td>

    <td>
        !{var destination = ship.getNav().getRoute().getDestination();}

        @if(ship.getNav().isInTransit())
            <a href="/galaxy/waypoint/${ship.getNav().getRoute().getOrigin().getSymbol()}">${ship.getNav().getRoute().getOrigin().getSymbol()}</a> ->
            <a href="/galaxy/waypoint/${destination.getSymbol()}">${destination.getSymbol()}</a>
        @else
            <a href="/galaxy/waypoint/${destination.getSymbol()}">${destination.getSymbol()} (${destination.getType()})</a>
        @endif
    </td>

    <td>
        <span class="select">
            <select id="flight-mode-select" disabled="${ship.isInTransit()}">
                @for(var flightMode : ShipNavFlightMode.values())
                    <option hx-swap="none" hx-post="/fleet/${ship.getSymbol()}/flight?mode=${flightMode.name()}"
                            value="${flightMode.name()}"
                            selected="${flightMode == ship.getNav().getFlightMode()}">${flightMode.name()}</option>
                @endfor
            </select>
        </span>
    </td>

    <td class="is-number">
        !{var fuel = ship.getFuel();}

        @if(!fuel.isInfinite())
            ${fuel.getCurrent()} / ${fuel.getCapacity()}
        @else
            &#x221e;
        @endif
    </td>

    <td class="is-number">
        !{var cargo = ship.getCargo();}

        @if(cargo.getCapacity() != 0)
            ${cargo.getUnits()} / ${cargo.getCapacity()}
        @else
            -
        @endif
    </td>

    <td>
        @if(ship.hasActiveCooldown())
            ${ActivityState.WAITING_ON_COOLDOWN}
        @elseif(ship.isInTransit())
            ${ShipNavStatus.IN_TRANSIT} (est
            <time class="humanize humanize-relativetime"
                  datetime="${ship.getNav().getRoute().getArrival().toString()}">${clock.differenceInSeconds(ship.getNav().getRoute().getArrival())}</time>)
        @elseif(!ship.getNav().isInTransit())
            ${ship.getNav().getStatus()}
        @else
            ${ActivityState.IDLE}
        @endif
    </td>

    <td>
        @if(!ship.isInTransit())
            <div class="dropdown is-hoverable">
                <div class="dropdown-trigger">
                    <button class="button" aria-haspopup="true" aria-controls="dropdown-menu-${ship.getSymbol()}">
                    <span class="icon is-small">
                        <i class="gg-more-vertical-alt" aria-hidden="true"></i>
                    </span>
                    </button>
                </div>
                <div id="dropdown-menu-${ship.getSymbol()}" class="dropdown-menu" role="menu">
                    <div class="dropdown-content">

                        @if(ship.isInOrbit())
                            <a class="dropdown-item" hx-post="/fleet/${ship.getSymbol()}/dock"
                               hx-target="#ship-row-${ship.getSymbol()}" hx-swap="outerHTML">⚓ Dock
                            </a>
                        @elseif(ship.isDocked())
                            <a class="dropdown-item" hx-post="/fleet/${ship.getSymbol()}/orbit"
                               hx-target="#ship-row-${ship.getSymbol()}" hx-swap="outerHTML">🛩️ Orbit
                            </a>
                        @endif

                        <hr class="dropdown-divider"/>

                        <a class="dropdown-item" hx-get="/fleet/${ship.getSymbol()}/navigate" hx-swap="innerHTML"
                           hx-target="#modal-container">🧭 Navigate
                        </a>

                        @if(!fuel.isInfinite() && ship.getNav().getRoute().getDestination().getType() == WaypointType.FUEL_STATION && ship.isDocked())
                            <a class="dropdown-item" hx-swap="none" hx-post="/fleet/${ship.getSymbol()}/refuel">⛽ Refuel
                            </a>
                        @endif

                        @if(automationSupport.contains(ship.getRole()))
                            <a href="/automation/${ship.getSymbol()}" class="dropdown-item icon-text">
                                 <span class="icon">
                                    <i class="gg-bot"></i>
                                </span>
                                <span>${JteLocalizer.i18n("fleet.automation")}</span>
                            </a>
                        @endif
                    </div>
                </div>
            </div>
        @endif
    </td>
</tr>
