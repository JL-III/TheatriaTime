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

All four share the same contract:

- **Immutable** — each event is a read-only snapshot.
- **Not cancellable** — events announce that a boundary has *already* been crossed.
- **Main-thread** — fired synchronously, so the Bukkit API is safe to call from handlers.

The day, week and month events carry **no payload by design**: each is fired at
the moment its boundary is crossed, so the firing itself is the signal (the
"when" is simply now). Only `HourChangeEvent` carries data, because the pair it
exposes describes a window the consumer cannot derive on its own:

- `getLastHour()` — the reset hour in effect before the change.
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
import com.playtheatria.theatriaTime.events.HourChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Duration;

public final class TimeListener implements Listener {

    @EventHandler
    public void onDayChange(DayChangeEvent event) {
        getLogger().info("A new day has begun.");
    }

    @EventHandler
    public void onHourChange(HourChangeEvent event) {
        Duration elapsed = Duration.between(event.getLastHour(), event.getNow());
        // ... react to the elapsed reset window
    }
}
```

Register it during `onEnable()`:

```java
getServer().getPluginManager().registerEvents(new TimeListener(), this);
```

> **Note:** Bukkit dispatches events by their concrete class, so register a
> handler for each concrete event you want to receive.

## Stability

Types and members in the `events` package follow semantic-versioning
expectations: existing accessors are preserved across minor releases, and
anything slated for removal is marked `@Deprecated` first.
