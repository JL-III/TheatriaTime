package com.playtheatria.theatriaTime;

import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import com.playtheatria.jliii.generalutils.utils.TimeUtils;
import com.playtheatria.theatriaTime.commands.ResetTimeCommand;
import com.playtheatria.theatriaTime.database.ResetTime;
import com.playtheatria.theatriaTime.database.ResetTimeRepository;
import com.playtheatria.theatriaTime.database.TheatriaTimeDB;
import com.playtheatria.theatriaTime.managers.ResetTimeManager;
import com.playtheatria.theatriaTime.managers.ConfigManager;
import com.playtheatria.theatriaTime.tasks.DatabaseTask;
import com.playtheatria.theatriaTime.tasks.HourChangeCheckTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public final class TheatriaTime extends JavaPlugin {
    private ResetTimeManager resetTimeManager;
    private ResetTimeRepository resetTimeRepository;
    private DatabaseTask databaseTask;
    private CustomLogger<TheatriaTime, ConfigManager> customLogger;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigManager configManager = new ConfigManager(this);
        customLogger = new CustomLogger<>(
                configManager,
                "#fc5203",
                "#fcb503",
                "#fff8bd"
        );
        TheatriaTimeDB theatriaTimeDB;
        // Ensure the data folder exists
        if (!getDataFolder().exists()) {
            boolean created = getDataFolder().mkdirs();
            if (created) {
                getLogger().info("Plugin data folder created at: " + getDataFolder().getAbsolutePath());
            } else {
                getLogger().severe("Failed to create plugin data folder: " + getDataFolder().getAbsolutePath());
            }
        }
        try {
            theatriaTimeDB = new TheatriaTimeDB(getDataFolder());
        } catch (IOException e) {
            customLogger.sendFormattedLog("Failed to create database: " + e.getMessage());
            e.printStackTrace();
            customLogger.sendFormattedLog("Shutting down...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        try {
            resetTimeRepository = new ResetTimeRepository(theatriaTimeDB, customLogger);
        } catch (SQLException e) {
            customLogger.sendFormattedLog("Failed to create sessionRepository: " + e.getMessage());
            customLogger.sendFormattedLog("Shutting down...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        ResetTime resetTime = resetTimeRepository.loadResetTime();
        if (resetTime == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            customLogger.sendFormattedLog("ResetTime returned null, this needs to be addressed. Shutting down the plugin.");
            return;
        }
        resetTimeManager = new ResetTimeManager(resetTime);
        databaseTask = new DatabaseTask(resetTimeRepository, resetTimeManager, customLogger);
        databaseTask.runTaskTimerAsynchronously(this, 20 * configManager.getInitialBackupDuration(), 20 * configManager.getBackupDuration());
        new HourChangeCheckTask(resetTimeManager, customLogger).runTaskTimer(this, 20 * 5, 20);
        Objects.requireNonNull(getCommand("theatria-time")).setExecutor(new ResetTimeCommand(resetTimeManager, configManager, customLogger));
        customLogger.sendFormattedLog("Loaded plugin.");
        customLogger.sendFormattedLog("Using GeneralUtils version: " + customLogger.getGeneralUtilsVersionFromConfig(getResource("plugin.yml"), "general-utils-version"));
    }

    @Override
    public void onDisable() {
        if (databaseTask != null) {
            databaseTask.cancel();
        }
        if (resetTimeRepository != null && resetTimeManager != null) {
            customLogger.sendFormattedLog(
                    String.format("Persisting data for the last reset time now: last reset hour %s, next reset hour %s",
                            TimeUtils.getFormat().format(resetTimeManager.getResetTime().getLastResetHour()),
                            TimeUtils.getFormat().format(resetTimeManager.getResetTime().getNextResetHour())
                    )
            );
            resetTimeRepository.saveResetTime(resetTimeManager.getResetTime());
        }
    }
}
