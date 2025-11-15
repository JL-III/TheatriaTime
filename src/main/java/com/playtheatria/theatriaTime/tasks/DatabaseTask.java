package com.playtheatria.theatriaTime.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.playtheatria.jliii.generalutils.utils.TimeUtils;
import com.playtheatria.theatriaTime.database.ResetTimeRepository;
import com.playtheatria.theatriaTime.managers.ResetTimeManager;

import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseTask extends BukkitRunnable {
    private final ResetTimeRepository resetTimeRepository;
    private final ResetTimeManager resetTimeManager;
    private static final Logger logger = Logger.getLogger(DatabaseTask.class.getName());

    public DatabaseTask(
            ResetTimeRepository resetTimeRepository,
            ResetTimeManager resetTimeManager
    ) {
        this.resetTimeRepository = resetTimeRepository;
        this.resetTimeManager = resetTimeManager;
    }

    @Override
    public void run() {
        logger.log(Level.INFO,
                String.format("Persisting data for the last reset time now: last reset hour %s, next reset hour %s",
                        TimeUtils.getFormat().format(resetTimeManager.getResetTime().getLastResetHour()),
                        TimeUtils.getFormat().format(resetTimeManager.getResetTime().getNextResetHour())
                )
        );
        logger.log(Level.INFO, "[run] Running on thread: {0}", Thread.currentThread().getName());
        long start = System.nanoTime();
        resetTimeRepository.saveResetTime(resetTimeManager.getResetTime());

        long end = System.nanoTime();
        long ms = (end - start) / 1_000_000L;

        logger.log(Level.INFO, "DatabaseTask duration: {0}ms", ms);
    }
}
