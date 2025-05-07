@import static dev.vinpol.nebula.dragonship.web.support.jte.RawContent.raw

@param String dataJson

@template.template(title = "Settings", content =  @`
    <div class="container is-widescreen">
        <section class="section">
            <h1 class="title is-1">Settings</h1>

            <fieldset class="field is-horizontal">
                <div class="field-label is-normal has-text-left">
                    <label for="token-input" class="label">Token</label>
                </div>

                <div id="token-input-field" class="field-body">
                </div>
            </fieldset>
        </section>
    </div>

    <span id="settings-data-json" class="is-hidden">
        ${raw(dataJson)}
    </span>
`, scripts = @`
    @template.asset(name = "settings/settings.js")
`)
