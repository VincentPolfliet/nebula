@import dev.vinpol.nebula.dragonship.web.Page
@import dev.vinpol.spacetraders.sdk.models.Ship
@import java.util.Collection
@import java.util.List

@param Page page
@param int currentPage
@param int total
@param List<Ship> ships

@template.template(page = page,
content = @`
    <div class="container is-widescreen">
        <h1 class="title">Fleet</h1>

        <table class="table">
            <thead>
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Role</th>
                <th scope="col">Cond / Int</th>
                <th scope="col">Location</th>
                <th scope="col">Flightmode</th>
                <th scope="col">Fuel</th>
                <th scope="col">Status</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>

            @for(var ship : ships)
                @template.fleet.ship-row(ship = ship)
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
