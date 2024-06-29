@param dev.vinpol.nebula.dragonship.web.HtmlPage page
@param String token

@template.template(page = page, content =  @`
    <div class="container is-widescreen">
        <section class="section">
            <h1 class="title is-1">Settings</h1>

            <fieldset class="field is-horizontal">
                <div class="field-label is-normal has-text-left">
                    <label for="token-input" class="label">Token</label>
                </div>

                <div class="field-body">
                    <div class="field has-addons">
                        <p class="control is-expanded">
                            <input id="token-input" class="input" name="token" type="password" placeholder="token"
                                   value="${token}">
                        </p>
                        <p class="control">
                            <a id="token-view-button" class="button" onclick="handleViewToken(event)">
                                View
                            </a>
                        </p>
                    </div>
                </div>
            </fieldset>

            <fieldset class="field is-horizontal">
                <div class="field-label is-normal has-text-left">
                    <label for="token-input" class="label">
                        Sync from API
                    </label>
                    <span class="subtitle is-6">Refresh the ship cache</span>
                </div>
                <div class="field-body">
                    <div class="field">
                        <p class="control">
                            <a id="token-view-button" class="button" hx-post="/api/sync" hx-swap="none">
                                Sync
                            </a>
                        </p>
                    </div>
                </div>
            </fieldset>
        </section>
    </div>
`, scripts = @`
    @template.asset(name = "main.js")

    <script>
        let viewTokenEnabled = false;

        function handleViewToken(event) {
            event.preventDefault();

            viewTokenEnabled = !viewTokenEnabled;

            const tokenInput = document.getElementById('token-input');
            tokenInput.type = viewTokenEnabled ? 'text' : 'password';

            const viewTokenButton = document.getElementById('token-view-button')
            viewTokenButton.innerText = viewTokenEnabled ? 'Hide' : 'View';
        }
    </script>
`)
