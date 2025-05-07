@import dev.vinpol.nebula.dragonship.web.agent.CurrentAgentAdvice
@import static dev.vinpol.nebula.dragonship.web.support.vite.StaticViteEnv.isDev
@import gg.jte.Content

@param String title = null
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

    <title>
        ${title}
    </title>

    @if(head != null)
        ${head}
    @endif
</head>
<body>

<div id="app">
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
</div>

@if(isDev())
    @template.asset(name = "@vite/client")
@endif

@template.asset(name = "modal.js")

@if(scripts != null)
    ${scripts}
@endif
</body>
</html>
