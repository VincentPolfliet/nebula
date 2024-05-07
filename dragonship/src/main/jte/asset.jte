@import static dev.vinpol.nebula.dragonship.web.config.support.vite.jte.JteManifestSupport.asset

@param String name
@param boolean local = false

@if(!local)
    ${asset(name)}
@else
    <script src="${name}"></script>
@endif
