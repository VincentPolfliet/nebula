@import dev.vinpol.nebula.dragonship.web.Page
@import dev.vinpol.spacetraders.sdk.models.System
@import java.util.List
@import java.util.stream.Collectors

@param Page page
@param int currentPage
@param int total
@param List<System> systems
@param boolean searchEnabled = false

@template.template(page = page, content = @`
    <div class="container is-widescreen">
        <div class="section">
            @if (searchEnabled)
                <div class="field is-grouped">
                    <p class="control is-expanded">
                        <input class="input" type="text" placeholder="Find a system">
                    </p>
                    <p class="control">
                        <button class="button is-info">
                            Search
                        </button>
                    </p>
                </div>
            @endif

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
