@import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour
@import dev.vinpol.nebula.dragonship.utils.time.TimeWizard
@import dev.vinpol.nebula.dragonship.web.fleet.FleetUtils
@import dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer
@import dev.vinpol.spacetraders.sdk.models.*
@import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode

@import java.util.Set
@import static dev.vinpol.nebula.dragonship.web.fleet.FleetUtils.*

@param Ship ship
@param Set<ShipRole> automationSupport
@param ShipBehaviour currentBehaviour = null
@param TimeWizard clock

<tr id="ship-row-${ship.getSymbol()}">
    <td><a href="/ship/detail/${ship.getSymbol()}">${ship.getSymbol()}</a></td>

    <td>${ship.getRole()}</td>

    <td>
        !{
           double reactorCondition = ship.getReactor().getCondition() * 100;
           double frameCondition = ship.getFrame().getCondition() * 100;
           double engineCondition = ship.getEngine().getCondition() * 100;
        }

        <ul class="condition-list">
            <li class="condition-item">${formatCondition(reactorCondition)}</li>
            <li class="condition-item">${formatCondition(frameCondition)}</li>
            <li class="condition-item-last">${formatCondition(frameCondition)}</li>
            <li class="condition-item-average">${formatCondition(calculateAverageCondition(ship))}</li>
        </ul>

        !{
            double reactorIntegrity = ship.getReactor().getIntegrity() * 100;
            double frameIntegrity = ship.getFrame().getIntegrity() * 100;
            double engineIntegrity = ship.getEngine().getIntegrity() * 100;
        }

        <ul class="condition-list">
            <li class="condition-item">${formatCondition(reactorIntegrity)}</li>
            <li class="condition-item">${formatCondition(frameIntegrity)}</li>
            <li class="condition-item-last">${formatCondition(engineIntegrity)}</li>
            <li class="condition-item-average">${formatCondition(calculateAverageIntegrity(ship))}</li>
        </ul>
    </td>

    <td>
        !{var origin = ship.getNav().getRoute().getOrigin();}
        !{var destination = ship.getNav().getRoute().getDestination();}

        @if(ship.getNav().isInTransit())
            <a href="/galaxy/waypoint/${origin.getSymbol()}">${origin.getSymbol()}</a> <i class="gg-arrow-right"></i>
            <a href="/galaxy/waypoint/${destination.getSymbol()}">${destination.getSymbol()}</a>
        @else
            <a href="/galaxy/waypoint/${destination.getSymbol()}">${destination.getSymbol()}
                <span>(${destination.getType()})</span></a>
        @endif
    </td>

    <td>
        <p>${ship.getNav().getStatus().name()}</p>

        @if(ship.isInTransit())
            !{var targetDate = ship.getNav().getRoute().getArrival();}
            <time class="humanize humanize-relativetime humanize-update is-small"
                  datetime="${targetDate.toString()}">${clock.differenceInSeconds(targetDate)}</time>
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

    <td>
        @if(ship.hasActiveCooldown())
            !{var targetDate = ship.getCooldown().getExpiration();}
            <time class="humanize humanize-relativetime humanize-update is-small"
                  datetime="${targetDate.toString()}">${clock.differenceInSeconds(targetDate)}</time>
        @endif
    </td>

    <td class="is-number">
        !{var fuel = ship.getFuel();}

        @if(fuel.isNotInfinite())
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
        <div class="level">
            <div class="level-left">
                @if(automationSupport.contains(ship.getRole()) && !ship.isInTransit())
                    <button class="button" hx-post="/fleet/${ship.getSymbol()}/automation/start"
                            hx-target="#ship-row-${ship.getSymbol()}" hx-swap="outerHTML"
                            disabled="${currentBehaviour != null}">
                        <span class="icon">
                                <i class="gg-play-button-o"></i>
                        </span>
                    </button>

                    <button class="button" hx-post="/fleet/${ship.getSymbol()}/automation/stop"
                            hx-target="#ship-row-${ship.getSymbol()}" hx-swap="outerHTML"
                            disabled="${currentBehaviour == null}">
                        <span class="icon">
                            <i class="gg-play-stop-o"></i>
                        </span>
                    </button>
                @endif
            </div>

            <div class="level-right">
                @if(!ship.isInTransit())
                    <div class="dropdown is-hoverable">
                        <div class="dropdown-trigger">
                            <button class="button" aria-haspopup="true"
                                    aria-controls="dropdown-menu-${ship.getSymbol()}">
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

                                <a class="dropdown-item" hx-get="/fleet/${ship.getSymbol()}/navigate"
                                   hx-swap="innerHTML"
                                   hx-target="#modal-container">🧭 Navigate
                                </a>

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
            </div>
        </div>
    </td>
</tr>
