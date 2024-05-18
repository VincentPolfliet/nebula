@import dev.vinpol.spacetraders.sdk.models.Ship
@import dev.vinpol.spacetraders.sdk.models.System
@import java.util.Objects

@param Ship ship
@param System system

<div class="modal is-active">
    <div class="modal-background"></div>
    <div class="modal-content">
        <form class="form" hx-target="#ship-row-${ship.getSymbol()}" hx-post="/fleet/${ship.getSymbol()}/navigate">
            <header class="modal-card-head">
                <span class="modal-card-title"><h1 class="title is-3">Navigation</h1></span>
                <button class="delete" aria-label="close" id="closeModalBtn"></button>
            </header>

            <section class="modal-card-body">
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
                            <select id="navigation-target-select" class="select" name="target">
                                <option selected></option>
                                @for(var waypoint : system.getWaypoints()))
                                    @if(!Objects.equals(waypoint.getSymbol(), ship.getNav().getWaypointSymbol()))
                                        <option value="${waypoint.getSymbol()}"
                                                hx-swap="innerHTML">${waypoint.getSymbol()}
                                            (${waypoint.getType().name()})
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
            </section>
            <footer class="modal-card-foot">
                <button class="is-primary button" type="submit">Navigate</button>
            </footer>
        </form>
    </div>
</div>
