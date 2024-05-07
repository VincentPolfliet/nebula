package dev.vinpol.spacetraders.sdk.api;

import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import dev.vinpol.spacetraders.sdk.utils.page.PageRequest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageIteratorTest {

    @Test
    void happy() {
        int total = 2;

        PageIterator<Object> iterator = new PageIterator<>(PageIterator.INITIAL_PAGE, 1, new Function<>() {

            private int counter = total;

            @Override
            public Page<Object> apply(PageRequest pageRequest) {
                if (counter > 0) {
                    --counter;
                    return new Page<>(Collections.singletonList(new Object()), total);
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
}
