package com.playtheatria.theatriaTime.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when TheatriaTime detects that a new week has begun.
 *
 * <p>This event carries no payload by design: it is fired at the moment the week
 * rolls over, so the firing itself is the signal. See the
 * {@linkplain com.playtheatria.theatriaTime.events package documentation} for
 * the contract shared by all TheatriaTime events (not cancellable, fired on the
 * main server thread).</p>
 *
 * @since 0.0.1
 */
public class WeekChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Creates a new week-change event. TheatriaTime fires these; consuming
     * plugins receive them through the Bukkit event bus.
     */
    public WeekChangeEvent() {
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
