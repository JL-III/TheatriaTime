/**
 * Public event API for the TheatriaTime plugin.
 *
 * <p>This package is the stable, supported integration surface that other
 * plugins compile and link against. Everything here is intended for external
 * consumption; the rest of the plugin (database, tasks, managers, commands) is
 * internal and may change without notice.</p>
 *
 * <h2>What it provides</h2>
 * <p>TheatriaTime tracks the passage of real-world time and fires a Bukkit event
 * each time a calendar boundary is crossed:</p>
 * <ul>
 *   <li>{@link com.playtheatria.theatriaTime.events.HourChangeEvent} &mdash;
 *       carries the previous reset hour and the detection time (the elapsed
 *       reset window).</li>
 *   <li>{@link com.playtheatria.theatriaTime.events.DayChangeEvent}</li>
 *   <li>{@link com.playtheatria.theatriaTime.events.WeekChangeEvent}</li>
 *   <li>{@link com.playtheatria.theatriaTime.events.MonthChangeEvent}</li>
 * </ul>
 * <p>The day, week and month events carry no payload by design: each is fired at
 * the moment its boundary is crossed, so the firing itself is the signal. Only
 * {@code HourChangeEvent} exposes data, because the pair it carries describes a
 * window that a consumer cannot derive on its own.</p>
 *
 * <h2>Shared contract</h2>
 * <p>Every event in this package:</p>
 * <ul>
 *   <li>is <b>immutable</b> &mdash; a read-only snapshot;</li>
 *   <li>is <b>not</b> {@link org.bukkit.event.Cancellable} &mdash; it announces
 *       that a boundary has <em>already</em> been crossed;</li>
 *   <li>is fired synchronously on the <b>main server thread</b>, so it is safe
 *       to call the Bukkit API from a handler.</li>
 * </ul>
 *
 * <h2>Consuming the API</h2>
 * <p>A consuming plugin compiles against TheatriaTime, declares it as a
 * dependency in its {@code plugin.yml} ({@code depend} or {@code softdepend}),
 * and registers a normal Bukkit listener:</p>
 * <pre>{@code
 * public final class TimeListener implements Listener {
 *     @EventHandler
 *     public void onDayChange(DayChangeEvent event) {
 *         // a new day has begun
 *     }
 *
 *     @EventHandler
 *     public void onHourChange(HourChangeEvent event) {
 *         Duration elapsed = Duration.between(event.getLastHour(), event.getNow());
 *         // ...
 *     }
 * }
 * }</pre>
 *
 * <h2>Stability</h2>
 * <p>Types and members in this package follow semantic-versioning expectations:
 * existing accessors are preserved across minor releases, and anything slated
 * for removal is marked {@link java.lang.Deprecated} first.</p>
 *
 * @since 0.0.1
 */
package com.playtheatria.theatriaTime.events;
