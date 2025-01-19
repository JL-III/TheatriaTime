package com.playtheatria.theatriaTime;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class TaskTimer extends BukkitRunnable {
    private final ResetTimeManager resetTimeManager;
    private final SessionManager sessionManager;
    private final Essentials essentials;

    public TaskTimer(ResetTimeManager resetTimeManager, SessionManager sessionManager, Essentials essentials) {
        this.resetTimeManager = resetTimeManager;
        this.sessionManager = sessionManager;
        this.essentials = essentials;
    }

    @Override
    public void run() {
        checkReset();
    }

    public void checkReset() {
        ResetTime resetTime = resetTimeManager.getResetTime();
        LocalDateTime now = LocalDateTime.now(Util.timeZone);

        if (now.isAfter(resetTime.getNextResetHour())) {
            Bukkit.getPluginManager().callEvent(new HourChangeEvent(resetTime.getLastResetHour(), now));
            resetTimeManager.setResetTime(new ResetTime());
        }
    }
}

