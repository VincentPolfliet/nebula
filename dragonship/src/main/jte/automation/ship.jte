@import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmDescription
@import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour
@import dev.vinpol.spacetraders.sdk.models.Ship

@param Ship ship
@param ShipAlgorithmDescription description
@param String possibleBehaviour
@param ShipBehaviour behaviour = null


@template.template(
content = @`
    <div class="container is-widescreen">
        <ul>
            @for(var reason : description.build())
                <li>${reason.reason()} - ${String.valueOf(reason.parameters())}</li>
            @endfor
        </ul>
    </div>
`,
scripts = @`@template.asset(name = "/automation/automation.js")`
)
