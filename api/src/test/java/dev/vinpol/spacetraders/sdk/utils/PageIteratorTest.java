package dev.vinpol.spacetraders.sdk.utils;

import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import dev.vinpol.spacetraders.sdk.utils.page.PageRequest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageIteratorTest {

    @Test
    void happy() {
        final int total = 2;

        PageIterator<String> iterator = new PageIterator<>(PageIterator.INITIAL_PAGE, 1, new Function<>() {

            private int counter = total;

            @Override
            public Page<String> apply(PageRequest pageRequest) {
                if (counter > 0) {
                    return new Page<>(Collections.singletonList("Item " + --counter), total);
                }

                return null;
            }
        });

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isInstanceOf(Object.class);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isInstanceOf(Object.class);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void firstRequestReturnsNull() {
        PageIterator<Object> pageIterator = new PageIterator<>(request -> null);

        assertThat(pageIterator.hasNext()).isFalse();
        assertThatThrownBy(pageIterator::next).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void returnsEmptyPage() {
        PageIterator<Object> pageIterator = new PageIterator<>(request -> Page.ofCollection(Collections.emptyList()));

        assertThat(pageIterator.hasNext()).isFalse();
        assertThatThrownBy(pageIterator::next).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void returnsPageWhereTotalIsEqualToSize() {
        var data = new Object();

        PageIterator<Object> pageIterator = new PageIterator<>(req -> Page.ofCollection(List.of(data)));

        assertThat(pageIterator.hasNext()).isTrue();
        assertThat(pageIterator.next()).isEqualTo(data);
        assertThat(pageIterator.hasNext()).isFalse();
    }

    @Test
    void multipleHasNextCalls() {
        AtomicInteger callCount = new AtomicInteger(0);

        PageIterator<String> iterator = new PageIterator<>(request -> {
            int count = callCount.incrementAndGet();

            if (count == 1) {
                return new Page<>(List.of("item1"), 1);
            }

            throw new AssertionError("Should not be called again!");
        });

        assertThat(iterator.hasNext()).isTrue();
        //noinspection ConstantValue
        assertThat(iterator.hasNext()).isTrue();

        assertThat(iterator.next()).isEqualTo("item1");

        assertThat(iterator.hasNext()).isFalse();

        assertThat(callCount.get()).isEqualTo(1);
    }

    @Test
    void multiplePages() {
        Map<Integer, List<String>> pages = new HashMap<>();
        pages.put(1, List.of("item1", "item2"));
        pages.put(2, List.of("item3"));

        TestPageFetcher<String> testPageFetcher = new TestPageFetcher<>(req -> {
            List<String> pageData = pages.get(req.page());

            if (pageData != null) {
                return new Page<>(pageData, 3); // Total of 3 items
            }

            return null;
        });

        PageIterator<String> iterator = new PageIterator<>(1, 2, testPageFetcher);

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo("item1");
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo("item2");

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo("item3");

        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void respectsMaxPageSize() {
        AtomicInteger requestedSize = new AtomicInteger(0);

        // Try to create with an oversized page
        PageIterator<String> iterator = new PageIterator<>(1, 30, request -> {
            requestedSize.set(request.size());
            return new Page<>(List.of("item"), 1);
        });

        assertThat(iterator.next()).isEqualTo("item");

        // Should be capped at MAX_SIZE (20)
        assertThat(requestedSize.get()).isEqualTo(PageIterator.MAX_SIZE);
    }
}
