package com.playtheatria.theatriaTime.tasks;

import com.playtheatria.jliii.generalutils.events.time.HourChangeEvent;
import com.playtheatria.jliii.generalutils.time.Utils;
import com.playtheatria.theatriaTime.database.ResetTime;
import com.playtheatria.theatriaTime.managers.ResetTimeManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class TaskTimer extends BukkitRunnable {
    private final ResetTimeManager resetTimeManager;

    public TaskTimer(ResetTimeManager resetTimeManager) {
        this.resetTimeManager = resetTimeManager;
    }

    @Override
    public void run() {
        checkReset();
    }

    public void checkReset() {
        ResetTime resetTime = resetTimeManager.getResetTime();
        LocalDateTime now = LocalDateTime.now(Utils.timeZone);

        if (now.isAfter(resetTime.getNextResetHour())) {
            Bukkit.getPluginManager().callEvent(new HourChangeEvent(resetTime.getLastResetHour(), now));
            resetTimeManager.setResetTime(new ResetTime());
        }
    }
}

