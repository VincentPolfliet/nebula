@import dev.vinpol.nebula.dragonship.web.Page

@param Page page

<!doctype html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="utf-8">

    <title>${page.getTitle()}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>

<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div id="navbarSupportedContent" class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="#">🌍 Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="/fleet">🚀 Fleet</a>
                </li>
            </ul>
            <form class="d-flex">
                <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success" type="submit">Search</button>
            </form>
        </div>
    </div>
</nav>

<main>
    <div id="tabs"></div>
</main>

<!-- FIXME for prod: this should be removed -->
@template.asset(name = "@vite/client")
@template.asset(name = "main.js")

</body>
</html>
