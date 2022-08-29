package ai.dot.dwtools.jooq;

import java.time.Instant;
import org.jooq.Converter;

import java.sql.Timestamp;

/**
 * A {@link org.jooq.Converter} for {@link java.time.Instant} objects.
 */
public class InstantConverter implements Converter<Timestamp, Instant> {
    @Override
    public Instant from(Timestamp timestamp) {
        return timestamp != null
                ? timestamp.toInstant()
                : null;
    }

    @Override
    public Timestamp to(Instant instant) {
        return instant != null
                ? Timestamp.from(instant)
                : null;
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<Instant> toType() {
        return Instant.class;
    }
}
