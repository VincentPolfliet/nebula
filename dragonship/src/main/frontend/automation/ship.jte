@import dev.vinpol.nebula.dragonship.automation.ActivityState
@import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour
@import dev.vinpol.spacetraders.sdk.models.Ship

@param Ship ship
@param ActivityState activityState
@param ShipBehaviour behaviour = null


@template.template(
title = ship.getSymbol() + " | " + "Automation",
content = @`
    <div class="container is-widescreen">
        <h1 class="title">Automation</h1>
        <h2 class="title is-4">${ship.getSymbol()}</h2>

        <p>${ship.getSymbol()} is currently ${activityState}.</p>

        <div class="buttons">
            <button class="button">
                        <span class="icon">
                            @if(behaviour == null)
                                <i class="gg-play-button-o"></i>
                            @else
                                <i class="gg-play-pause-o"></i>
                            @endif
                        </span>
                <span>${behaviour == null ? "Start" : "Stop"}</span>
            </button>

            <button class="button" disabled="${behaviour == null}">
                    <span class="icon">
                        <i class="gg-play-stop-o"></i>
                    </span>
                <span>Stop</span>
            </button>
        </div>


        <h5 class="subtitle is-5">Current Run</h5>
        @if(behaviour != null)
        @else
            <p>${ship.getSymbol()} is currently ${activityState}</p>
        @endif
    </div>
`,
scripts = @`@template.asset(name = "/automation/automation.js")`
)
