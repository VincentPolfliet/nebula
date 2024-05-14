@import dev.vinpol.nebula.dragonship.web.Page
@import static dev.vinpol.nebula.dragonship.web.config.support.jte.RawContent.raw

@param String map

@template.template(page = new Page().withTitle("Map"),
head = @`
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
          integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY=" crossorigin=""/>`,
content = @`
    <div id="app"></div>

    <span id="galaxy-data" class="is-hidden">
        ${raw(map)}
    </span>
`,
scripts = @`
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
            integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo=" crossorigin=""></script>
    @template.asset(name = "/galaxy/map.js")
`)
