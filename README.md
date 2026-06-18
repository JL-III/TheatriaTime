# TheatriaTime

A Paper/Spigot plugin that tracks the passage of real-world time on a Minecraft
server and fires Bukkit events when calendar boundaries are crossed (hour, day,
week, month). Other plugins consume those events as a stable, public API.

## Event API

The integration surface lives in
[`com.playtheatria.theatriaTime.events`](src/main/java/com/playtheatria/theatriaTime/events).
Everything else in the plugin (database, tasks, managers, commands) is internal
and may change without notice.

| Event             | Fires when…                          |
|-------------------|--------------------------------------|
| `HourChangeEvent` | the tracked reset hour has elapsed   |
| `DayChangeEvent`  | a new day has begun                  |
| `WeekChangeEvent` | a new week has begun                 |
| `MonthChangeEvent`| a new month has begun                |

All four extend `TheatriaTimeEvent`, which defines the shared contract:

- **Immutable** — each event is a read-only snapshot.
- **Not cancellable** — events announce that a boundary has *already* been crossed.
- **Main-thread** — fired synchronously, so the Bukkit API is safe to call from handlers.
- **Carries context** — every event exposes:
  - `getLastResetHour()` — the reset boundary in effect before the change.
  - `getNow()` — the moment the change was detected (server time zone).

## Consuming the API

### 1. Depend on TheatriaTime

Add TheatriaTime as a `compileOnly` dependency in your build (it is published to
GitHub Packages as `com.playtheatria:theatriatime`):

```groovy
repositories {
    maven {
        name = "TheatriaTime"
        url = "https://maven.pkg.github.com/JL-III/TheatriaTime"
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly("com.playtheatria:theatriatime:<version>")
}
```

### 2. Declare the dependency in `plugin.yml`

```yaml
depend: [TheatriaTime]      # or softdepend, if TheatriaTime is optional
```

### 3. Register a listener

```java
import com.playtheatria.theatriaTime.events.DayChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class TimeListener implements Listener {

    @EventHandler
    public void onDayChange(DayChangeEvent event) {
        getLogger().info("A new day began at " + event.getNow());
    }
}
```

Register it during `onEnable()`:

```java
getServer().getPluginManager().registerEvents(new TimeListener(), this);
```

> **Note:** Bukkit dispatches events by their concrete class. Register a handler
> for each concrete event you want to receive — you cannot register a single
> handler against the abstract `TheatriaTimeEvent` and receive its subclasses.

## Stability

Types and members in the `events` package follow semantic-versioning
expectations: existing accessors are preserved across minor releases, and
anything slated for removal is marked `@Deprecated` first (for example
`HourChangeEvent.getLastHour()`, superseded by `getLastResetHour()`).
