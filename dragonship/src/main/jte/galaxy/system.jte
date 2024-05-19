@import dev.vinpol.nebula.dragonship.web.Page
@import dev.vinpol.spacetraders.sdk.models.System
@import dev.vinpol.spacetraders.sdk.models.WaypointType
@import java.util.List

@param Page page
@param System system

@template.template(page = page, content = @`
    <div class="container is-widescreen">
        <div class="section">

            <h1 class="title">System</h1>

            <div class="columns">
                <div class="column">
                    <a class="block" href="/galaxy/system/${system.getSymbol()}/map">
                        <h1 class="title is-4">
                            🧭 Map
                        </h1>
                    </a>
                </div>
                <div class="column">
                    <a class="block">
                        <h1 class="title is-4">
                            🛒 Markets
                        </h1>
                    </a>
                </div>
                <div class="column">
                    <a class="block" href="/galaxy/system/${system.getSymbol()}/map">
                        <h1 class="title is-4">
                            TODO
                        </h1>
                    </a>
                </div>
            </div>
        </div>
    </div>
`, scripts = @`@template.asset(name = "main.js")`)
