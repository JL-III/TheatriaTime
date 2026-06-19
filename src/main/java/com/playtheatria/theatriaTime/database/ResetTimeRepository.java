package com.playtheatria.theatriaTime.database;

import com.j256.ormlite.dao.Dao;
import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import com.playtheatria.theatriaTime.TheatriaTime;
import com.playtheatria.theatriaTime.managers.ConfigManager;

import java.sql.SQLException;

public class ResetTimeRepository {
    private final Dao<ResetTime, String> dao;
    private final CustomLogger<TheatriaTime, ConfigManager> customLogger;

    public ResetTimeRepository(
            TheatriaTimeDB theatriaTimeDB,
            CustomLogger<TheatriaTime, ConfigManager> customLogger
    ) throws SQLException {
        this.dao = theatriaTimeDB.getDao(ResetTime.class);
        this.customLogger = customLogger;
    }

    public ResetTime loadResetTime() {
        try {
            ResetTime resetTime = dao.queryForId("0");
            if (resetTime == null) {
                customLogger.sendFormattedLog("No ResetTime found in database. Creating a new ResetTime.");
                resetTime = new ResetTime();
                dao.create(resetTime);
            }
            return resetTime;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Forces ORMLite to build and class-load the statements it initialises
     * lazily (notably {@code MappedUpdate}) the first time each operation type
     * is used.
     *
     * <p>Call this once during {@code onEnable()}, while the plugin's class
     * loader is healthy. Otherwise the first {@code update} of the session can
     * happen inside {@code onDisable()} — for example when the server is stopped
     * before the first scheduled backup runs — at which point Paper has begun
     * tearing the plugin down and the class loader can no longer resolve new
     * classes, so the lazy load fails with a {@link NoClassDefFoundError}.</p>
     */
    public void warmUp() {
        try {
            ResetTime existing = dao.queryForId("0");
            if (existing != null) {
                // Idempotent: rewrites the same row, but loads the update path.
                dao.update(existing);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void saveResetTime(ResetTime resetTime) {
        customLogger.sendDebug("[save] Running on thread: " + Thread.currentThread().getName());
        try {
            ResetTime existing = dao.queryForId("0");
            if (existing == null) {
                customLogger.sendFormattedLog("No ResetTime found. Creating a new ResetTime.");
                dao.create(resetTime);
            } else {
                customLogger.sendDebug("Backing up reset time, this is normal.");
                dao.update(resetTime);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
