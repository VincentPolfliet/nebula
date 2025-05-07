@import static dev.vinpol.nebula.dragonship.web.utils.PagingUtils.calculatePages
@import static dev.vinpol.nebula.dragonship.web.support.jte.JteLocalizer.i18n

@param int page
@param int pageSize
@param int total
@param int pageOffset = 2
@param String url = ""

!{var pages = calculatePages(page, pageOffset, pageOffset, total); }

@if(total > 1)
    <nav class="pagination is-centered" role="navigation" aria-label="pagination">
        !{boolean previousIsAvailable = page - 1 > 0;}
        <a href="${url}?page=${page - 1}&pageSize=${pageSize}"
           class="pagination-previous ${previousIsAvailable ? "" : "is-disabled"} prev">${i18n("page.prev")}</a>

        !{boolean nextIsAvailable = page + 1 < total;}
        <a href="${url}?page=${page + 1}&pageSize=${pageSize}"
           class="pagination-next ${nextIsAvailable ? "" : "is-disabled"} next">${i18n("page.next")}</a>

        <ul class="pagination-list">
            @if(page - (pageOffset /* startOffset */ + 1 /* currentPage */) > 0)
                <li>
                    <a href="${url}?page=1&pageSize=${pageSize}" class="pagination-link"
                       aria-label="Page 1">1</a>
                </li>

                <li>
                    <span class="pagination-ellipsis">&hellip;</span>
                </li>
            @endif

            @for(int currentPage : pages)
                <li>
                    <a href="${url}?page=${currentPage}&pageSize=${pageSize}"
                       class="pagination-link ${currentPage == page ? "is-current": "" }"
                       aria-label="Page ${currentPage}"
                       aria-current="${currentPage == page ? String.valueOf(page) : "" }">${currentPage}</a>
                </li>
            @endfor

            @if(page + (pageOffset /* endOffset */) < total)
                <li>
                    <span class="pagination-ellipsis">&hellip;</span>
                </li>

                <li>
                    <a href="${url}?page=${total}&pageSize=${pageSize}" class="pagination-link"
                       aria-label="Page ${total}">${total}</a>
                </li>
            @endif
        </ul>
    </nav>
@endif

