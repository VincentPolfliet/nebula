@import dev.vinpol.nebula.dragonship.web.HtmlPage
@import dev.vinpol.spacetraders.sdk.models.System
@import java.util.List
@import java.util.stream.Collectors

@param String title
@param int currentPage
@param int limit
@param int total
@param List<System> systems

@template.template(title = title, content = @`
    <div class="container is-widescreen">
        <h1 class="title">Systems</h1>

        <table class="table  is-fullwidth">
            <thead>
            <tr>
                <th scope="col">Symbol</th>
                <th scope="col">Name</th>
                <th scope="col">Sector</th>
                <th scope="col">Type</th>
                <th scope="col">Types</th>
            </tr>
            </thead>
            <tbody>
            @for(var system : systems)
                <tr>
                    <td><a href="/galaxy/system/${system.getSymbol()}">${system.getSymbol()}</a></td>
                    <td>${system.getName()}</td>
                    <td>${system.getSectorSymbol()}</td>
                    <td>${system.getType()}</td>
                    <td>${system.getWaypoints().stream().map(s -> s.getType().getValue()).distinct().sorted().collect(Collectors.joining(", "))}</td>
                </tr>
            @endfor
            </tbody>
        </table>

        @template.pagination(page = currentPage, pageSize = limit, total = total, url = "/galaxy/systems")
    </div>
`, scripts = @`@template.asset(name = "main.js")`)
