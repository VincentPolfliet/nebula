@import dev.vinpol.spacetraders.sdk.models.Agent
@import static dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer.i18n

@param Agent agent

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
            <div class="navbar-item has-dropdown is-hoverable">
                <a class="navbar-item">
                    👤 ${agent.getSymbol()}
                </a>

                <div class="navbar-dropdown">
                    <div class="p-4">
                        <div class="fixed-grid">
                            <div class="grid">
                                <div class="has-text-weight-bold cell">${i18n("credits")}</div>
                                <div class="cell">${agent.getCredits()}</div>
                                <div class="has-text-weight-bold cell">${i18n("hq")}</div>
                                <div class="cell">
                                    <a href="/galaxy/waypoint/${agent.getHeadquarters()}">${agent.getHeadquarters()}</a>
                                </div>
                                <div class="has-text-weight-bold cell">${i18n("faction")}</div>
                                <div class="cell">${agent.getStartingFaction()}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <a href="/settings" class="navbar-item">
                ⚙️
            </a>
        </div>
    </div>
</nav>
