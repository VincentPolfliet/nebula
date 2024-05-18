@import dev.vinpol.nebula.dragonship.utils.StringUtils
@import static dev.vinpol.nebula.dragonship.web.support.vite.jte.JteManifestSupport.asset

@param String name
@param boolean local = false

@if(!local)
    ${asset(name)}
@else
    <script src="${StringUtils.appendIfNotStartsWith('/', name)}"></script>
@endif
