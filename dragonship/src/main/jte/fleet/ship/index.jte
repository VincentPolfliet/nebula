@import dev.vinpol.spacetraders.sdk.models.Ship

@param Ship ship

@template.template(content = @`
    <div class="container is-widescreen">
        <h1 class="title is-1">${ship.getSymbol()}</h1>
        <h3 class="subtitle is-3">${ship.getRole()}</h3>


        <div>
            <h2 class="title is-2">Reactor</h2>
            !{var reactor = ship.getReactor();}

            <div class="block">
                <span class="tag">⚡ ${reactor.getPowerOutput()}</span>
            </div>

            <p>${reactor.getName()}</p>

            <p>${reactor.getDescription()}</p>
        </div>

        <div class="columns">
            <div class="column">
                <h2 class="title is-2">Modules</h2>

                @for(var module : ship.getModules())
                    <details>
                        <summary class="title is-5">
                            ${module.getName()}
                            <span class="subtitle is-6">(${module.getSymbol()})</span>
                        </summary>

                        <p>${module.getDescription()}</p>
                    </details>
                @endfor
            </div>
            <div class="column">
                <h2 class="title is-2">Mounts</h2>

                @for(var mount : ship.getMounts())
                    <details>
                        <summary class="title is-5">
                            ${mount.getName()}
                            <span class="subtitle is-6">(${mount.getSymbol()})</span>
                        </summary>

                        <p>${mount.getDescription()}</p>
                    </details>
                @endfor
            </div>
        </div>


    </div>
`,
scripts = @`@template.asset(name = "main.js")`)
