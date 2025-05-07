package dev.vinpol.spacetraders.sdk.utils.page;

import dev.vinpol.spacetraders.sdk.models.ApiPageable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An iterator that retrieves items in pages from a paginated data source.
 * This implementation is not threadsafe. If the remote source is modified while iterating,
 * the iterator may skip or miss items.
 *
 * <p>The iterator uses a {@link PageRequest} to fetch pages of data, with each page containing
 * a specified number of items (page size). The total number of items is initially unknown and
 * is determined from the first fetched page.</p>
 *
 * <p>This class implements {@link Iterator}, {@link Iterable}, and {@link Spliterator} interfaces,
 * allowing it to be used in for-each loops, streams, and other contexts that require these interfaces.</p>
 *
 * @param <T> The type of elements returned by this iterator
 */

@NotThreadSafe
public final class PageIterator<T> implements Iterator<T>, Iterable<T>, Spliterator<T> {

    public static final int INITIAL_PAGE = 1;
    public static final int INITIAL_SIZE = 10;
    public static final int MAX_SIZE = 20;

    private static final int UNKNOWN_TOTAL_SIZE = -1;

    private int currentPage;
    private int processedItems = 0;

    private final int pageSize;
    private int totalSize;
    private Iterator<T> currentIterator;

    private final Function<PageRequest, Page<T>> pageFetch;

    public PageIterator(Function<PageRequest, Page<T>> pageFetch) {
        this(INITIAL_PAGE, INITIAL_SIZE, pageFetch);
    }

    public PageIterator(int page, int size, Function<PageRequest, Page<T>> pageFetch) {
        this.currentPage = page;
        this.pageSize = Math.min(size, MAX_SIZE);
        this.pageFetch = pageFetch;
        this.totalSize = UNKNOWN_TOTAL_SIZE;
    }

    @NotNull
    public static <T> PageIterator<T> iterate(Function<PageRequest, Page<T>> pageFetchFunction) {
        Objects.requireNonNull(pageFetchFunction);

        return new PageIterator<>(pageFetchFunction);
    }

    @NotNull
    public static <T> PageIterator<T> iterate(PageRequest request, Function<PageRequest, Page<T>> pageFetchFunction) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(pageFetchFunction);

        return new PageIterator<>(request.page(), request.size(), pageFetchFunction);
    }

    public static <T> PageIterator<T> iterate(BiFunction<Integer, Integer, ApiPageable<T>> pageRequestPageFunction) {
        Objects.requireNonNull(pageRequestPageFunction);

        return new PageIterator<>(req -> {
            ApiPageable<T> pageable = pageRequestPageFunction.apply(req.page(), req.size());
            return new Page<>(pageable.getData(), pageable.getMeta().total());
        });
    }

    public static <T> Stream<T> stream(Function<PageRequest, Page<T>> pageFetcher) {
        PageIterator<T> contractIterable = PageIterator.iterate(pageFetcher);
        return StreamSupport.stream(contractIterable, false);
    }


    public static <T> Stream<T> stream(int page, int pageSize, Function<PageRequest, Page<T>> pageFetcher) {
        PageIterator<T> contractIterable = PageIterator.iterate(new PageRequest(page, pageSize), pageFetcher);
        return StreamSupport.stream(contractIterable, false);
    }

    @Override
    public boolean hasNext() {
        if (currentIterator != null && currentIterator.hasNext()) {
            return true;
        }

        if (totalSize != UNKNOWN_TOTAL_SIZE && processedItems >= totalSize) {
            return false;
        }

        // fetching in hasNext violates the contract that an iterable has,
        // but this makes it work like you would expect an iterator to work
        PageRequest request = new PageRequest(currentPage++, pageSize);
        Page<T> page = pageFetch.apply(request);

        if (page != null) {
            if (totalSize == UNKNOWN_TOTAL_SIZE) {
                totalSize = page.total();
            }

            if (totalSize == 0) {
                return false;
            }

            currentIterator = page.data().iterator();
            processedItems += page.data().size();
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

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T t : this) {
            action.accept(t);
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        while (hasNext()) {
            action.accept(next());
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (hasNext()) {
            action.accept(next());
            return true;
        }

        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        // Cannot split this iterator as it needs to process pages sequentially
        return null;
    }

    @Override
    public long estimateSize() {
        if (totalSize == UNKNOWN_TOTAL_SIZE) {
            return Long.MAX_VALUE;
        }
        return totalSize - processedItems;
    }

    @Override
    public int characteristics() {
        return ORDERED | NONNULL | IMMUTABLE;
    }
}
