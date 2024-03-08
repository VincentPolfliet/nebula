package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class WaypointSymbolTest {

    @Test
    void tryParse() {
        String input = "X1-DF55-20250Z";

        WaypointSymbol actual = WaypointSymbol.tryParse(input);

        assertThat(actual.sector()).isEqualTo("X1");
        assertThat(actual.system()).isEqualTo("X1-DF55");
        assertThat(actual.waypoint()).isEqualTo("X1-DF55-20250Z");
    }

    @Test
    void tryParseNullThrowsException() {
        NullPointerException exception = catchNullPointerException(() -> WaypointSymbol.tryParse(null));

        assertThat(exception).isNotNull();
    }

    @Test
    void tryParseNoSplittableInThreeThrowsException() {
        IllegalArgumentException exception = catchIllegalArgumentException(() -> WaypointSymbol.tryParse("X1-DD"));

        assertThat(exception).isNotNull();
    }
}
