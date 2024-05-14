@import dev.vinpol.nebula.dragonship.web.Page
@import dev.vinpol.spacetraders.sdk.models.System
@import java.util.List
@import java.util.stream.Collectors

@param Page page
@param int currentPage
@param int total
@param List<System> systems

@template.template(page = page, content = @`
    <div class="container is-widescreen">
        <div class="section">
            <h1 class="title">Systems</h1>

            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Symbol</th>
                    <th scope="col">Sector</th>
                    <th scope="col">Type</th>
                    <th scope="col">Coordinates</th>
                    <th scope="col">Types</th>
                </tr>
                </thead>
                <tbody>
                @for(var system : systems)
                    <tr>
                        <td><a href="/galaxy/system/${system.getSymbol()}">${system.getSymbol()}</a></td>
                        <td>${system.getSectorSymbol()}</td>
                        <td>${system.getType()}</td>
                        <td>[${system.getX()}, ${system.getY()}]</td>
                        <td>${system.getWaypoints().stream().map(s -> s.getType().getValue()).distinct().sorted().collect(Collectors.joining(", "))}</td>
                    </tr>
                @endfor
                </tbody>
            </table>

            @template.pagination(page = currentPage, total = total, url = "/galaxy/systems")
        </div>
    </div>
`, scripts = @`@template.asset(name = "main.js")`)
