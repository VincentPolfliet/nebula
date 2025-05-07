@import dev.vinpol.nebula.dragonship.web.fleet.SystemWaypointWithDistance
@import dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer
@import dev.vinpol.spacetraders.sdk.models.Ship
@import java.util.*
@import java.util.Collections
@import java.util.List
@import java.util.Objects
@import static dev.vinpol.nebula.dragonship.web.fleet.FleetUtils.formatDistance
@import static dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer.i18n
@import dev.vinpol.nebula.dragonship.web.galaxy.TravelCostEstimate
@import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode

@param Ship ship
@param List<SystemWaypointWithDistance> waypoints

@template.modal(
title = "Navigation",
content = @`
    <form id="ship-row-${ship.getSymbol()}-form" class="form" hx-target="#ship-row-${ship.getSymbol()}"
          hx-post="/fleet/${ship.getSymbol()}/navigate"
          hx-swap="outerHTML">
        <div class="field">
            <div class="control has-icons-left">
                <input id="origin-waypoint" class="input" type="text"
                       value="${ship.getNav().getWaypointSymbol()}"
                       disabled>
                <span class="icon is-small is-left"><label for="origin-waypoint">🛫</label></span>
            </div>
        </div>

        <div class="field">
            <div class="control has-icons-left is-expanded">
                <div class="select is-fullwidth">
                    <select id="navigation-target-select" class="select" hx-target="#travel-estimate-container"
                            name="target" hx-trigger="change" hx-swap="innerHTML" hx-indicator="#loading-indicator"
                            hx-get="/fleet/${ship.getSymbol()}/navigate/estimate">
                        <option value="" selected></option>
                        @for(var waypoint : waypoints))
                            @if(!Objects.equals(waypoint.getSymbol(), ship.getNav().getWaypointSymbol()))
                                <option value="${waypoint.getSymbol()}">${waypoint.getSymbol()}
                                    - ${formatDistance(waypoint.distance())} AU (${waypoint.getType().name()})
                                </option>
                            @endif
                        @endfor
                    </select>

                    <span class="icon is-small is-left">
                        <label for="navigation-target-select">🛬</label>
                    </span>
                </div>
            </div>
        </div>

        <div id="travel-estimate-container">
            <div class="pb-5">
                @template.fleet.navigate-table()
            </div>
        </div>

        <div id="loading-indicator" class="htmx-indicator columns is-centered is-vcentered is-mobile">
            <div class="column is-narrow has-text-centered">
                <div class="sk-chase">
                    <div class="sk-chase-dot"></div>
                    <div class="sk-chase-dot"></div>
                    <div class="sk-chase-dot"></div>
                    <div class="sk-chase-dot"></div>
                    <div class="sk-chase-dot"></div>
                    <div class="sk-chase-dot"></div>
                </div>
            </div>
        </div>
    </form>
`,
footer = @`
    <button form="ship-row-${ship.getSymbol()}-form" class="is-primary button"
            type="submit">${i18n("fleet.navigate.text")}</button>`
)
