package com.playtheatria.theatriaTime.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * Fired when TheatriaTime detects that the tracked reset hour has elapsed and a
 * new one has begun. This is the most frequent TheatriaTime event.
 *
 * <p>Unlike the day/week/month events, this one carries a payload: the previous
 * reset hour together with the moment the change was detected. The pair
 * describes the reset <em>window</em> that just elapsed, which is useful for
 * work that accrues over time. See the
 * {@linkplain com.playtheatria.theatriaTime.events package documentation} for
 * the contract shared by all TheatriaTime events (immutable, not cancellable,
 * fired on the main server thread).</p>
 *
 * @since 0.0.1
 */
public class HourChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalDateTime lastHour;
    private final LocalDateTime now;

    /**
     * Creates a new hour-change event.
     *
     * @param lastResetHour the reset hour that was in effect before this change;
     *                      must not be {@code null}
     * @param now           the moment, in the server's configured time zone, at
     *                      which the new hour was detected; must not be
     *                      {@code null}
     */
    public HourChangeEvent(@NotNull LocalDateTime lastResetHour, @NotNull LocalDateTime now) {
        this.lastHour = lastResetHour;
        this.now = now;
    }

    /**
     * Returns the reset hour that was in effect before this change.
     *
     * @return the previous reset hour; never {@code null}
     */
    public @NotNull LocalDateTime getLastHour() {
        return lastHour;
    }

    /**
     * Returns the moment at which TheatriaTime detected this change, expressed
     * in the server's configured time zone.
     *
     * @return the time the event was fired; never {@code null}
     */
    public @NotNull LocalDateTime getNow() {
        return now;
    }

    /**
     * Returns the handler list for this event type.
     *
     * @return the shared {@link HandlerList}; never {@code null}
     */
    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
