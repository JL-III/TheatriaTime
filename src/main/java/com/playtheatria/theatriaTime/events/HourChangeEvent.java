package com.playtheatria.theatriaTime.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * Fired when TheatriaTime detects that the tracked hour has elapsed and a new
 * reset hour has begun. This is the most frequent TheatriaTime event.
 *
 * <p>See {@link TheatriaTimeEvent} for the contract shared by every TheatriaTime
 * event (immutability, threading and the data accessors).</p>
 *
 * @since 0.0.1
 */
public class HourChangeEvent extends TheatriaTimeEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Creates a new hour-change event.
     *
     * @param lastResetHour the reset hour in effect before this change
     * @param now           the moment the new hour was detected
     */
    public HourChangeEvent(@NotNull LocalDateTime lastResetHour, @NotNull LocalDateTime now) {
        super(lastResetHour, now);
    }

    /**
     * Returns the reset hour that was in effect before this change.
     *
     * @return the previous reset hour; never {@code null}
     * @deprecated Use {@link #getLastResetHour()} instead. This alias is
     *             retained for backward compatibility and will be removed in a
     *             future release; {@code getLastResetHour()} is the canonical
     *             accessor shared by all {@link TheatriaTimeEvent}s.
     */
    @Deprecated
    public @NotNull LocalDateTime getLastHour() {
        return getLastResetHour();
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
