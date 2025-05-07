@import static dev.vinpol.nebula.dragonship.web.support.jte.RawContent.raw

@param String map

@template.template(title = "Galaxy",
content = @`
    <div id="galaxy-app"></div>

    <span id="galaxy-data" class="is-hidden">
        ${raw(map)}
    </span>
`,
scripts = @`
    @template.asset(name = "/galaxy/map.js")
`)
