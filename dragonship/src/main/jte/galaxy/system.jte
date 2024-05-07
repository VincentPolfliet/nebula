@import dev.vinpol.nebula.dragonship.web.Page
@import dev.vinpol.spacetraders.sdk.models.System
@import java.util.List

@param Page page
@param System system

@template.template(page = page, content = @`
    <div class="container is-widescreen">
        <div class="section">

            <h1 class="title">System</h1>

            <a class="button is-primary" href="/galaxy/system/${system.getSymbol()}/map">🧭 Map</a>

            <table  class="table">
                <thead>
                <tr>
                    <th scope="col">Symbol</th>
                    <th scope="col">Orbit</th>
                    <th scope="col">Type</th>
                    <th scope="col">Orbitals</th>
                </tr>
                </thead>
                <tbody>
                @for(var waypoint : system.getWaypoints())
                    <tr>
                        <td>${waypoint.getSymbol()}</td>
                        <td>${waypoint.getOrbits()}</td>
                        <td>${waypoint.getType()}</td>
                        <td>
                            @for(var orbital : waypoint.getOrbitals())
                               ${orbital.getSymbol()},
                            @endfor
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
`, scripts = @`@template.asset(name = "main.js")`)
