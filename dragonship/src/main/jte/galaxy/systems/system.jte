@import dev.vinpol.nebula.dragonship.web.HtmlPage
@import dev.vinpol.spacetraders.sdk.models.System
@import dev.vinpol.spacetraders.sdk.models.Waypoint
@import java.util.List

@param HtmlPage page
@param System system
@param List<Waypoint> waypoints

@template.template(page = page, content = @`
    <div class="container is-widescreen">
        <div class="section">

            <div class="fixed-grid has-auto-count">
                <div class="grid">
                    @for(var waypoint : waypoints)
                        <div class="cell is-col-min-3">
                            <div class="card">
                                <header class="card-header">
                                    <p class="card-header-title">${waypoint.getSymbol()}</p>
                                    <button class="card-header-icon" aria-label="more options">
                                    </button>
                                </header>
                                <div class="card-content">
                                    <div class="content">
                                        <a href="#">@bulmaio</a>. <a href="#">#css</a> <a href="#">#responsive</a>
                                        <br/>
                                        <time datetime="2016-1-1">11:09 PM - 1 Jan 2016</time>
                                    </div>
                                </div>
                                <footer class="card-footer buttons">
                                    <button class="buton is-primary">Navigate</button>
                                </footer>
                            </div>
                        </div>
                    @endfor
                </div>
            </div>
        </div>
    </div>
`, scripts = @`@template.asset(name = "main.js")`)
