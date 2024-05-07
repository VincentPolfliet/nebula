package dev.vinpol.spacetraders.sdk.utils.page;

import dev.vinpol.spacetraders.sdk.models.ApiPageable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class PageIterator<T> implements Iterator<T>, Iterable<T> {

    public static final int INITIAL_PAGE = 1;
    public static final int INITIAL_SIZE = 10;

    private int currentPage;
    private final int pageSize;
    private int totalSize;
    private Iterator<T> currentIterator;

    private final Function<PageRequest, Page<T>> pageFetch;

    public PageIterator(Function<PageRequest, Page<T>> pageFetch) {
        this(INITIAL_PAGE, INITIAL_SIZE, pageFetch);
    }

    public PageIterator(int page, int size, Function<PageRequest, Page<T>> pageFetch) {
        this.currentPage = page;
        this.pageSize = size;
        this.pageFetch = pageFetch;
        this.totalSize = -1; // Initialize to -1 to indicate that total size is unknown
    }

    @NotNull
    public static <T> Iterable<T> iterate(Function<PageRequest, Page<T>> pageFetchFunction) {
        Objects.requireNonNull(pageFetchFunction);

        return new PageIterator<>(pageFetchFunction);
    }

    public static <T> Iterable<T> iterable(BiFunction<Integer, Integer, Page<T>> pageBiConsumer) {
        Objects.requireNonNull(pageBiConsumer);

        return new PageIterator<>(req -> pageBiConsumer.apply(req.page(), req.size()));
    }

    public static <T> Iterable<T> iterate(BiFunction<Integer, Integer, ApiPageable<T>> pageRequestPageFunction) {
        Objects.requireNonNull(pageRequestPageFunction);

        return new PageIterator<>(req -> {
            ApiPageable<T> pageable = pageRequestPageFunction.apply(req.page(), req.size());
            return new Page<>(pageable.getData(), pageable.getMeta().getTotal());
        });
    }

    public static <T> Stream<T> stream(Function<PageRequest, Page<T>> pageFetcher) {
        Iterable<T> contractIterable = PageIterator.iterate(pageFetcher);
        return StreamSupport.stream(contractIterable.spliterator(), false);
    }

    @Override
    public boolean hasNext() {
        // If currentIterator has elements, return true
        if (currentIterator != null && currentIterator.hasNext()) {
            return true;
        }

        // If totalSize is known and currentPage * pageSize exceeds totalSize, return false
        if (totalSize != -1 && currentPage * pageSize > totalSize) {
            return false;
        }

        // Fetch the next page
        PageRequest request = new PageRequest(currentPage++, pageSize);
        Page<T> page = pageFetch.apply(request);

        if (page != null) {
            // Update total size if it's not known
            if (totalSize == -1) {
                totalSize = page.total();
            }

            if (totalSize == 0) {
                return false;
            }

            // Initialize currentIterator with the data of the fetched page
            currentIterator = page.data().iterator();
            return true;
        }

        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return currentIterator.next();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
