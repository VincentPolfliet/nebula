@import dev.vinpol.nebula.dragonship.web.Page
@import gg.jte.Content

@param Page page = new dev.vinpol.nebula.dragonship.web.Page().withTitle("*** NO TITLE ***")
@param Content head = null
@param Content content = null
@param Content scripts = null

<!doctype html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="utf-8">
    <meta name="color-scheme" content="light dark"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>${page.getTitle()}</title>

    @if(head != null)
        ${head}
    @endif
</head>
<body>

<header id="header">
    @template.navbar()
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
