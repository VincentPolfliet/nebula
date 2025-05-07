package dev.vinpol.spacetraders.sdk.utils;

import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class TestPageFetcher<T> implements Function<PageRequest, Page<T>> {

    private final Logger logger = LoggerFactory.getLogger(TestPageFetcher.class);

    private final Function<PageRequest, Page<T>> dataSupplier;
    private final List<PageRequest> requestHistory = new ArrayList<>();
    private final AtomicInteger callCount = new AtomicInteger(0);

    /**
     * Creates a new wrapper with the specified data supplier.
     *
     * @param dataSupplier The function that supplies page data
     */
    public TestPageFetcher(Function<PageRequest, Page<T>> dataSupplier) {
        this.dataSupplier = Objects.requireNonNull(dataSupplier);
    }

    /**
     * Factory method to create a data generator that produces pages with sequence numbers.
     *
     * @param totalItems Total number of items to generate
     * @param <E>        Type of generated elements
     * @return A function that produces pages of generated data
     */
    public static <E> Function<PageRequest, Page<E>> generateSequence(int totalItems, Function<Integer, E> itemGenerator) {
        return request -> {
            int startIndex = (request.page() - 1) * request.size();
            if (startIndex >= totalItems) {
                return null; // No more data
            }

            int endIndex = Math.min(startIndex + request.size(), totalItems);
            int itemCount = endIndex - startIndex;

            List<E> items = new ArrayList<>(itemCount);
            for (int i = 0; i < itemCount; i++) {
                E item = itemGenerator.apply(startIndex + i + 1);
                items.add(item);
            }

            return new Page<>(items, totalItems);
        };
    }

    @Override
    public Page<T> apply(PageRequest request) {
        int currentCall = callCount.incrementAndGet();
        requestHistory.add(request);

        if (logger.isDebugEnabled()) {
            logger.debug("Call #{}: Fetching page {} with size {}", currentCall, request.page(), request.size());
        }

        // Get data from the underlying supplier
        Page<T> result = dataSupplier.apply(request);

        if (logger.isDebugEnabled()) {
            if (result == null) {
                logger.debug("Call #{}: No page found", currentCall);
            } else {
                logger.debug("Call #{}: Found page {}", currentCall, result);
            }
        }

        return result;
    }

    public int getCallCount() {
        return callCount.get();
    }

    public List<PageRequest> getRequestHistory() {
        return Collections.unmodifiableList(requestHistory);
    }

    /**
     * @return The most recent page request, or null if none
     */
    public PageRequest getLastRequest() {
        if (requestHistory.isEmpty()) {
            return null;
        }
        return requestHistory.get(requestHistory.size() - 1);
    }

    public void reset() {
        callCount.set(0);
        requestHistory.clear();
    }
}
