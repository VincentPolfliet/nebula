@import static dev.vinpol.nebula.dragonship.web.config.support.vite.StaticViteEnv.isDev

@param dev.vinpol.spacetraders.sdk.models.Contract contract

!{var id = "contract-row-" + contract.getId();}

<tr id="${id}">
    <td>${contract.getType()}</td>

    !{var terms = contract.getTerms();}

    <td>${contract.getDeadlineToAccept().toString()}</td>
    <td>${terms.getPayment().getOnAccepted().toString()}</td>
    <td>${terms.getPayment().getOnFulfilled().toString()}</td>
    <td>${terms.getDeadline().toString()}</td>

    !{var deliverment = terms.getDeliver();}

    <td>
        <ul>
            @for(var item : deliverment)
                <li>
                    ${item.getUnitsRequired()} ${item.getTradeSymbol()}
                    at ${item.getDestinationSymbol()}
                </li>
            @endfor
        </ul>
    </td>

    <td>
        <div class="buttons">
            @if(!contract.getAccepted())
                <button hx-post="/contracts/${contract.getId()}" class="button is-primary" hx-target="#${id}">Accept</button>
            @endif
        </div>
    </td>

    @if(isDev())
        <td>
            @template.checked(val = contract.getAccepted())
        </td>
        <td>
            @template.checked(val = contract.getFulfilled())
        </td>
    @endif
</tr>
