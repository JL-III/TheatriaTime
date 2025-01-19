package com.playtheatria.theatriaTime.tasks;

import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import com.playtheatria.jliii.generalutils.utils.TimeUtils;
import com.playtheatria.theatriaTime.TheatriaTime;
import com.playtheatria.theatriaTime.database.ResetTimeRepository;
import com.playtheatria.theatriaTime.managers.ResetTimeManager;
import com.playtheatria.theatriaTime.managers.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseTask extends BukkitRunnable {
    private final ResetTimeRepository resetTimeRepository;
    private final ResetTimeManager resetTimeManager;
    private final CustomLogger<TheatriaTime, ConfigManager> customLogger;

    public DatabaseTask(
            ResetTimeRepository resetTimeRepository,
            ResetTimeManager resetTimeManager,
            CustomLogger<TheatriaTime, ConfigManager> customLogger
    ) {
        this.resetTimeRepository = resetTimeRepository;
        this.resetTimeManager = resetTimeManager;
        this.customLogger = customLogger;
    }

    @Override
    public void run() {
        customLogger.sendFormattedLog(
                String.format("Persisting data for the last reset time now: last reset hour %s, next reset hour %s",
                        TimeUtils.getFormat().format(resetTimeManager.getResetTime().getLastResetHour()),
                        TimeUtils.getFormat().format(resetTimeManager.getResetTime().getNextResetHour())
                )
        );
        resetTimeRepository.saveResetTime(resetTimeManager.getResetTime());
    }
}
