@import dev.vinpol.spacetraders.sdk.models.Ship
@import dev.vinpol.spacetraders.sdk.models.System
@import java.util.Objects

@param Ship ship
@param System system

@template.modal(
title = @`<h1 class="title is-3">Navigation</h1>`,
content = @`
    <form class="form" hx-target="#ship-row-${ship.getSymbol()}" hx-post="/fleet/${ship.getSymbol()}/navigate"
          hx-swap="outerHTML">
        <div class="field">
            <div class="control has-icons-left">
                <input id="origin-waypoint" class="input" type="text" value="${ship.getNav().getWaypointSymbol()}"
                       disabled>
                <span class="icon is-small is-left">
                    <label for="origin-waypoint">🛫</label>
                </span>
            </div>
        </div>

        <div class="field">
            <div class="control has-icons-left is-expanded">
                <div class="select is-fullwidth">
                    <select id="navigation-target-select" class="select" name="target">
                        <option selected></option>
                        @for(var waypoint : system.getWaypoints()))
                            @if(!Objects.equals(waypoint.getSymbol(), ship.getNav().getWaypointSymbol()))
                                <option value="${waypoint.getSymbol()}"
                                        hx-get="/fleet/fuel/estimation?ship=${ship.getSymbol()}&system=${waypoint.getSymbol().substring(0, 7)}&origin=${ship.getNav().getWaypointSymbol()}&target=${waypoint.getSymbol()}"
                                        hx-target="#fuel-estimation"
                                        hx-swap="innerHTML">${waypoint.getSymbol()}</option>
                            @endif
                        @endfor
                    </select>

                    <span class="icon is-small is-left">
                        <label for="navigation-target-select">🛬</label>
                    </span>
                </div>
            </div>
        </div>
    </form>

    <div id="fuel-estimation" class="pt-5">
        @template.fleet.fuel-estimation()
    </div>
`,
footer = @`
    <button class="is-primary button">Navigate</button>
`)
