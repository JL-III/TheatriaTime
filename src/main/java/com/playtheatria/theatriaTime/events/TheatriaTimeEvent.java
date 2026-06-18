package com.playtheatria.theatriaTime.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * Base type for every time-progression event published by TheatriaTime.
 *
 * <p>TheatriaTime tracks the passage of real-world time on the server and
 * announces each calendar boundary it crosses (hour, day, week, month) by
 * firing a concrete subclass of this event on the Bukkit event bus. Other
 * plugins react to the passage of time simply by listening for the event(s)
 * they care about &mdash; there is no need to call into TheatriaTime directly.</p>
 *
 * <h2>Contract</h2>
 * <ul>
 *   <li><b>Immutable.</b> Every event is a read-only snapshot. The two
 *       timestamps it carries are supplied at construction and never change.</li>
 *   <li><b>Not cancellable.</b> These events are notifications that a boundary
 *       has <em>already</em> been crossed, so they intentionally do not
 *       implement {@link Cancellable}.</li>
 *   <li><b>Main-thread.</b> They are fired synchronously on the main server
 *       thread, so it is safe to touch the Bukkit API from your handler.</li>
 *   <li><b>Non-null data.</b> {@link #getLastResetHour()} and {@link #getNow()}
 *       never return {@code null}.</li>
 * </ul>
 *
 * <h2>Listening</h2>
 * <p>This base type exists to document the shared contract, to expose the
 * common accessors in one place, and to let you treat received events
 * polymorphically (for example with {@code instanceof}). Bukkit dispatches
 * events by their concrete class, so you must register a handler for each
 * concrete event you want to receive &mdash; you cannot register a single
 * handler against {@code TheatriaTimeEvent} and receive its subclasses.</p>
 *
 * <pre>{@code
 * public final class MyListener implements Listener {
 *     @EventHandler
 *     public void onDayChange(DayChangeEvent event) {
 *         getLogger().info("A new day began at " + event.getNow());
 *     }
 * }
 * }</pre>
 *
 * @since 0.0.1
 */
public abstract class TheatriaTimeEvent extends Event {

    private final LocalDateTime lastResetHour;
    private final LocalDateTime now;

    /**
     * Creates a new event snapshot.
     *
     * <p>TheatriaTime is responsible for constructing and firing these events;
     * consuming plugins receive them through the Bukkit event bus and should
     * not normally instantiate them.</p>
     *
     * @param lastResetHour the reset boundary that was in effect immediately
     *                      before this change was detected; must not be
     *                      {@code null}
     * @param now           the moment, in the server's configured time zone, at
     *                      which the change was detected; must not be
     *                      {@code null}
     */
    protected TheatriaTimeEvent(@NotNull LocalDateTime lastResetHour, @NotNull LocalDateTime now) {
        this.lastResetHour = lastResetHour;
        this.now = now;
    }

    /**
     * Returns the reset boundary that was current immediately before this
     * change was detected.
     *
     * @return the previous reset hour; never {@code null}
     */
    public @NotNull LocalDateTime getLastResetHour() {
        return lastResetHour;
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
}
