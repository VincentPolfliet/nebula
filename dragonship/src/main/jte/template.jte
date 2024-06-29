@import dev.vinpol.nebula.dragonship.web.HtmlPage
@import dev.vinpol.nebula.dragonship.web.agent.CurrentAgentAdvice
@import dev.vinpol.spacetraders.sdk.models.Agent
@import gg.jte.Content

@param HtmlPage page = new dev.vinpol.nebula.dragonship.web.HtmlPage().withTitle("*** NO TITLE ***")
@param Content head = null
@param Content content = null
@param Content scripts = null

<!doctype html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="utf-8">
    <meta name="color-scheme" content="light dark"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
    <link rel="manifest" href="/site.webmanifest">

    <title>${page.getTitle()}</title>

    @if(head != null)
        ${head}
    @endif
</head>
<body>

<header id="header">
    @template.navbar(agent = CurrentAgentAdvice.getCurrentAgent())
</header>

<main id="main">
    @if(content != null)
        ${content}
    @endif
</main>
<footer id="footer">

</footer>

<!-- FIXME for prod: this should be removed -->
@template.asset(name = "@vite/client")
@template.asset(name = "modal.js", local= true)

@if(scripts != null)
    ${scripts}
@endif
</body>
</html>
