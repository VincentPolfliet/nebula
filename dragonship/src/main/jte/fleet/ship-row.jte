@import dev.vinpol.nebula.dragonship.automation.ActivityState
@import dev.vinpol.spacetraders.sdk.models.Ship
@import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode
@import dev.vinpol.spacetraders.sdk.models.ShipNavStatus

@import static dev.vinpol.nebula.dragonship.web.fleet.FleetUtils.*

@param Ship ship

<tr id="ship-row-${ship.getSymbol()}">
    <td class="is-vcentered"><a href="/ship/detail/${ship.getSymbol()}">${ship.getSymbol()}</a></td>
    <td class="is-vcentered">${ship.getRole()}</td>
    <td class="is-vcentered">
        ${String.format("%.0f", calculateAverageCondition(ship))}
        / ${String.format("%.0f", calculateAverageIntegrity(ship))}
    </td>

    <td class="is-vcentered">
        <a href="/galaxy/system/${ship.getNav().getSystemSymbol()}?target=${ship.getNav().getWaypointSymbol()}">
            ${ship.getNav().getWaypointSymbol()}
        </a>
    </td>

    <td class="is-vcentered">
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

    <td class="is-vcentered">
        !{var fuel = ship.getFuel();}

        @if(!fuel.isInfinite())
            ${ship.getFuel().getCurrent()} / ${ship.getFuel().getCapacity()}
        @else
            &#x221e;
        @endif
    </td>

    <td class="is-vcentered">
        @if(ship.hasActiveCooldown())
            ${ActivityState.WAITING_ON_COOLDOWN}
        @elseif(ship.isInTransit())
            <span
                class="humanize-duration">${ship.getNav().getRoute().getDurationInSeconds()}</span> until arriving at ${ship.getNav().getRoute().getDestination().getSymbol()}
        @else
            ${ActivityState.IDLE} (${ship.getNav().getStatus()})
        @endif
    </td>

    <td class="is-vcentered">
        <button class="button is-primary" hx-get="/fleet/${ship.getSymbol()}/navigate" hx-swap="innerHTML"
                hx-target="#modal-container">
            Navigate
        </button>

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
                    @if(!ship.isInTransit())
                        <div class="dropdown-item">
                            @if(ship.isInOrbit())
                                <a class="dropdown-item" hx-post="/fleet/${ship.getSymbol()}/dock"
                                   hx-target="#ship-row-${ship.getSymbol()}" hx-swap="outerHTML">⚓ Dock
                                </a>
                            @elseif(ship.isDocked())
                                <a class="dropdown-item" hx-post="/fleet/${ship.getSymbol()}/orbit"
                                   hx-target="#ship-row-${ship.getSymbol()}" hx-swap="outerHTML">🛩️ Orbit
                                </a>
                            @endif
                        </div>
                    @endif
                </div>
            </div>
        </div>
    </td>
</tr>
