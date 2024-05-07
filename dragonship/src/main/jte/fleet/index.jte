@import dev.vinpol.nebula.dragonship.web.Page
@import dev.vinpol.spacetraders.sdk.utils.page.PageRequest

@param Page page
@param PageRequest pageRequest

@template.template(page = page,
content = @`<span class="is-loading" max="100" hx-target="this" hx-swap="outerHTML" hx-trigger="load"
                  hx-get="/fleet.htmx?page=${pageRequest.page()}&total=${pageRequest.size()}"></span>`,
scripts = @`@template.asset(name = "main.js")`
)
