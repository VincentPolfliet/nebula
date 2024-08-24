@import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour
@import dev.vinpol.nebula.dragonship.utils.time.TimeWizard
@import dev.vinpol.nebula.dragonship.web.HtmlPage
@import dev.vinpol.spacetraders.sdk.models.Ship
@import dev.vinpol.spacetraders.sdk.models.ShipRole
@import java.util.List
@import java.util.Map
@import java.util.Set

@param HtmlPage page
@param int currentPage
@param int total
@param List<Ship> ships
@param Set<ShipRole> automationSupport
@param Map<Ship, ShipBehaviour> behaviours
@param TimeWizard clock

@template.template(page = page,
content = @`
    <div class="container is-widescreen">
        <h1 class="title">Fleet</h1>

        <table class="table is-fullwidth">
            <thead>
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Role</th>
                <th scope="col">Condition / Integrity</th>
                <th scope="col">Location</th>
                <th scope="col">Nav</th>
                <th scope="col">Flightmode</th>
                <th scope="col">Cooldown</th>
                <th scope="col">Fuel</th>
                <th scope="col">Cargo</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            @for(var ship : ships)
                @template.fleet.ship-row(ship = ship, clock = clock, automationSupport = automationSupport, currentBehaviour = behaviours.getOrDefault(ship, null))
            @endfor
            </tbody>
        </table>

        @template.pagination(page = currentPage, total = total)
    </div>

    <div id="modal-container">

    </div>
`,
scripts = @`
    @template.asset(name = "/fleet/fleet.js")
`
)
