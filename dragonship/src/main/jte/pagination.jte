@import static dev.vinpol.nebula.dragonship.web.utils.PagingUtils.calculatePages

@param int page
@param int total
@param int pageOffset = 2
@param String url = ""

!{var pages = calculatePages(page, pageOffset, pageOffset, total); }

@if(total > 1)
    <nav class="pagination" role="navigation" aria-label="pagination">
        @if(page - 1 > 0)
            <a href="${url}?page=${page - 1}" class="pagination-previous">Previous</a>
        @endif

        @if(page + 1 <= total)
            <a href="${url}?page=${page + 1}" class="pagination-next">Next page</a>
        @endif

        <ul class="pagination-list">
            @if(page - (pageOffset /* startOffset */ + 1 /* currentPage */) > 0)
                <li>
                    <a href="${url}?page=1" class="pagination-link"
                       aria-label="Page 1">1</a>
                </li>

                <li>
                    <span class="pagination-ellipsis">&hellip;</span>
                </li>
            @endif

            @for(int currentPage : pages)
                <li>
                    <a href="${url}?page=${currentPage}"
                       class="pagination-link ${currentPage == page ? "is-current": "" }"
                       aria-label="Page ${currentPage}"
                       aria-current="${currentPage == page ? String.valueOf(page) : "" }">${currentPage}</a>
                </li>
            @endfor

            @if(page + (pageOffset /* endOffset */ + 1 /* currentPage */) < total)
                <li>
                    <span class="pagination-ellipsis">&hellip;</span>
                </li>

                <li>
                    <a href="${url}?page=${total}" class="pagination-link"
                       aria-label="Page ${total}">${total}</a>
                </li>
            @endif
        </ul>
    </nav>
@endif

