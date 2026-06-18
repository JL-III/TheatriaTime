package com.playtheatria.theatriaTime.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * Fired when TheatriaTime detects that a new month has begun.
 *
 * <p>See {@link TheatriaTimeEvent} for the contract shared by every TheatriaTime
 * event (immutability, threading and the data accessors).</p>
 *
 * @since 0.0.1
 */
public class MonthChangeEvent extends TheatriaTimeEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Creates a new month-change event.
     *
     * @param lastResetHour the reset hour in effect before the month rolled over
     * @param now           the moment the new month was detected
     */
    public MonthChangeEvent(@NotNull LocalDateTime lastResetHour, @NotNull LocalDateTime now) {
        super(lastResetHour, now);
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
