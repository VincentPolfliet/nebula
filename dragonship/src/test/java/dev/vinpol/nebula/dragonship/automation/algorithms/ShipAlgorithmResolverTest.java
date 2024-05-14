package dev.vinpol.nebula.dragonship.automation.algorithms;

import dev.vinpol.spacetraders.sdk.models.ShipRole;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;

class ShipAlgorithmResolverTest {

    ShipAlgorithmResolver sut;

    @Test
    void hasAlgorithmForRole() {
        ShipAlgorithm expected = Mockito.mock(ShipAlgorithm.class);

        sut = new ShipAlgorithmResolver(Map.of(ShipRole.EXCAVATOR, expected));
        ShipAlgorithm actual = sut.resolve(ShipRole.EXCAVATOR);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void hasNoAlgorithmForRole() {
        sut = new ShipAlgorithmResolver(Collections.singletonMap(ShipRole.EXCAVATOR, null));
        ShipAlgorithm actual = sut.resolve(ShipRole.EXCAVATOR);

        assertThat(actual).isNull();
    }

    @Test
    void throwsExceptionWhenRoleIsNull() {
        sut = new ShipAlgorithmResolver(Collections.emptyMap());

        assertThatNullPointerException()
            .isThrownBy(() -> sut.resolve(null));
    }
}
