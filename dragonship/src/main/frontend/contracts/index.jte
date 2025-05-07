@import dev.vinpol.nebula.dragonship.web.HtmlPage
@import java.util.List
@import dev.vinpol.spacetraders.sdk.models.Contract
@import static dev.vinpol.nebula.dragonship.web.support.vite.StaticViteEnv.isDev

@param HtmlPage page
@param List<Contract> contracts

@template.template(title = page.getTitle(), content = @`
    <div class="container is-widescreen">
        <div class="section">
            <h1 class="title">Contracts</h1>

            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Type</th>
                    <th scope="col">Deadline to Accept</th>
                    <th scope="col">on Accepted</th>
                    <th scope="col">on Fulfilled</th>
                    <th scope="col">Deadline</th>
                    <th scope="col">Deliver</th>
                    <th scope="col">@template.checked(val = true)</th>

                    @if(isDev())
                        <th scope="col">Accepted?</th>
                        <th scope="col">Fullfilled?</th>
                    @endif
                </tr>
                </thead>
                <tbody>
                @for(var contract : contracts)
                    @template.contracts.contract-row(contract = contract)
                @endfor
                </tbody>
            </table>
        </div>
    </div>
`,
scripts = @`@template.asset(name = "main.js")`
)
