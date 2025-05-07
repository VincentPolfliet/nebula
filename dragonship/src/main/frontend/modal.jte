@import gg.jte.Content

@param String title = null
@param Content content
@param Content footer = null
@param boolean isActive = true

<div class="modal ${isActive ? "is-active" : ""}">
    <div class="modal-background">

    </div>
    <div class="modal-content">
        <header class="modal-card-head">
            <span class="modal-card-title">
                @if(title != null)
                    <h1 class="title is-3">${title}</h1>
                @endif
            </span>
            <button class="delete" aria-label="close" id="model-close-button"></button>
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
