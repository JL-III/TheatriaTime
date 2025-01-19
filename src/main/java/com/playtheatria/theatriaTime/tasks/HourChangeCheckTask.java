package com.playtheatria.theatriaTime.tasks;

import com.playtheatria.jliii.generalutils.events.time.HourChangeEvent;
import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import com.playtheatria.jliii.generalutils.utils.TimeUtils;
import com.playtheatria.theatriaTime.TheatriaTime;
import com.playtheatria.theatriaTime.database.ResetTime;
import com.playtheatria.theatriaTime.managers.ConfigManager;
import com.playtheatria.theatriaTime.managers.ResetTimeManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class HourChangeCheckTask extends BukkitRunnable {
    private final ResetTimeManager resetTimeManager;
    private final CustomLogger<TheatriaTime, ConfigManager> customLogger;

    public HourChangeCheckTask(ResetTimeManager resetTimeManager, CustomLogger<TheatriaTime, ConfigManager> customLogger) {
        this.resetTimeManager = resetTimeManager;
        this.customLogger = customLogger;
    }

    @Override
    public void run() {
        checkReset();
    }

    public void checkReset() {
        ResetTime resetTime = resetTimeManager.getResetTime();
        LocalDateTime now = LocalDateTime.now(TimeUtils.timeZone);

        if (now.isAfter(resetTime.getNextResetHour())) {
            customLogger.sendDebug("Hour change detected, firing hour change event!");
            Bukkit.getPluginManager().callEvent(new HourChangeEvent(resetTime.getLastResetHour(), now));
            resetTimeManager.setResetTime(new ResetTime());
        }
    }
}

