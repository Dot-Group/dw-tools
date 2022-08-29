package ai.dot.dwtools.jooq;

import java.time.Instant;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static com.google.common.truth.Truth.assertThat;

public class InstantConverterTest {
    private InstantConverter converter;

    @Before
    public void setup() {
        converter = new InstantConverter();
    }

    @Test
    public void roundTripFromTimestampWorks() {
        Timestamp ts = new Timestamp(12345L);
		Instant dt = converter.from(ts);

        assertThat(converter.to(dt)).isEqualTo(ts);
    }

    @Test
    public void roundTripFromDateTimeWorks() {
		Instant dt = Instant.now();
        Timestamp ts = converter.to(dt);

        assertThat(converter.from(ts)).isEqualTo(dt);
    }

    @Test
    public void convertsFromTimestamp() {
        assertThat(converter.fromType()).isEqualTo(Timestamp.class);
    }

    @Test
    public void convertsToDateTime() {
        assertThat(converter.toType()).isEqualTo(Instant.class);
    }

    @Test
    public void nullTimestampReturnsNullDateTime() {
        assertThat(converter.from(null)).isNull();
    }

    @Test
    public void nullDateTimeReturnsNullTimestamp() {
        assertThat(converter.to(null)).isNull();
    }
}
