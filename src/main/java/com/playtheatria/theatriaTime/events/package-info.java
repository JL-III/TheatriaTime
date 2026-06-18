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
 *   <li>{@link com.playtheatria.theatriaTime.events.HourChangeEvent}</li>
 *   <li>{@link com.playtheatria.theatriaTime.events.DayChangeEvent}</li>
 *   <li>{@link com.playtheatria.theatriaTime.events.WeekChangeEvent}</li>
 *   <li>{@link com.playtheatria.theatriaTime.events.MonthChangeEvent}</li>
 * </ul>
 * <p>All of them extend
 * {@link com.playtheatria.theatriaTime.events.TheatriaTimeEvent}, which defines
 * the shared contract: the events are immutable, are not cancellable, are fired
 * on the main server thread, and each carries the previous reset hour
 * ({@code getLastResetHour()}) and the time the change was detected
 * ({@code getNow()}).</p>
 *
 * <h2>Consuming the API</h2>
 * <p>A consuming plugin compiles against TheatriaTime, declares it as a
 * dependency in its {@code plugin.yml} ({@code depend} or {@code softdepend}),
 * and registers a normal Bukkit listener:</p>
 * <pre>{@code
 * public final class TimeListener implements Listener {
 *     @EventHandler
 *     public void onHourChange(HourChangeEvent event) {
 *         // react to the new hour ...
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
