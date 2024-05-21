@import dev.vinpol.nebula.dragonship.utils.time.TimeWizard
@import dev.vinpol.nebula.dragonship.web.Page
@import dev.vinpol.spacetraders.sdk.models.Ship
@import dev.vinpol.spacetraders.sdk.models.ShipRole
@import java.util.List
@import java.util.Set

@param Page page
@param int currentPage
@param int total
@param List<Ship> ships
@param Set<ShipRole> automationSupport
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
                <th scope="col">Condition</th>
                <th scope="col">Integrity</th>
                <th scope="col">Location</th>
                <th scope="col">Flightmode</th>
                <th scope="col">Fuel</th>
                <th scope="col">Cargo</th>
                <th scope="col">Status</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>

            @for(var ship : ships)
                @template.fleet.ship-row(ship = ship, clock = clock, automationSupport = automationSupport)
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
