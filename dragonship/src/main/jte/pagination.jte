@param int page
@param int total
@param int pageOffset = 1
@param String url = "#"

<nav class="pagination" role="navigation" aria-label="pagination">
    @if(page - 1 > 0)
        <a href="${url}?page=${page - 1}" class="pagination-previous">Previous</a>
    @endif

    @if(page + 1 <= total)
        <a href="${url}?page=${page + 1}" class="pagination-next">Next page</a>
    @endif

    <ul class="pagination-list">
        @if(page - 4 > 0)
            <li>
                <a href="${url}?page=1" class="pagination-link" aria-label="Goto page 1">1</a>
            </li>
        @endif

        @if(page - 3 > 1)
            <li>
                <span class="pagination-ellipsis">&hellip;</span>
            </li>
        @endif

        @for(int i = Math.max(1, page - pageOffset); i <= Math.min(page + pageOffset, total); ++i)
            <li>
                <a href="${url}?page=${i}" class="pagination-link ${i == page ? "is-current": "" }"
                   aria-label="Page ${i}"
                   aria-current="${i == page ? String.valueOf(page) : "" }">${i}</a>
            </li>
        @endfor

        @if(page + 3 < total)
            <li>
                <span class="pagination-ellipsis">&hellip;</span>
            </li>
        @endif

        @if(page + 4 <= total)
            <li>
                <a href="${url}?page=${total}" class="pagination-link" aria-label="Goto page ${total}">${total}</a>
            </li>
        @endif
    </ul>
</nav>

