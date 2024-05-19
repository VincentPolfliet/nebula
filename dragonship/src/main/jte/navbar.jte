@import static dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer.i18n

<nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
        <a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false" data-target="navbar-content">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
        </a>
    </div>

    <div id="navbar-content" class="navbar-menu">
        <div class="navbar-start">
            <a href="https://docs.spacetraders.io/" class="navbar-item" target="_blank">📄 ${i18n("nav.docs")}</a>
        </div>

        <div class="navbar-center">
            <a href="/" class="navbar-item">🏠 ${i18n("nav.home")}</a>
            <a href="/fleet" class="navbar-item">🚀 ${i18n("nav.fleet")}</a>
            <a href="/galaxy" class="navbar-item">🌌 ${i18n("nav.galaxy")}</a>
        </div>

        <div class="navbar-end">
            <a href="/settings" class="navbar-item">⚙️ ${i18n("nav.settings")}</a>
        </div>
    </div>
</nav>
