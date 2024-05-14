@param gg.jte.Content title = null
@param gg.jte.Content content
@param gg.jte.Content footer = null
@param boolean isActive = true

<div class="modal  ${isActive ? "is-active" : ""}">
    <div class="modal-background"></div>
    <div class="modal-content">
        <header class="modal-card-head">
            <span class="modal-card-title">
                @if(title != null)
                    ${title}
                @endif
            </span>
            <button class="delete" aria-label="close" id="closeModalBtn"></button>
        </header>

        <section class="modal-card-body">
            ${content}
        </section>

        <footer class="modal-card-foot">
            @if(footer != null)
                ${footer}
            @endif
        </footer>
    </div>
</div>
