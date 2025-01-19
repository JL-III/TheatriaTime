package com.playtheatria.theatriaTime.managers;

import com.playtheatria.jliii.generalutils.config.AbstractConfigManager;
import com.playtheatria.theatriaTime.TheatriaTime;

public class ConfigManager extends AbstractConfigManager<TheatriaTime> {
    private final TheatriaTime plugin;
    private final long backupDuration;
    private final long initialBackupDuration;

    public ConfigManager(TheatriaTime plugin) {
        super(plugin);
        this.plugin = plugin;
        this.backupDuration = plugin.getConfig().getLong("backup-duration");
        this.initialBackupDuration = plugin.getConfig().getLong("initial-backup-duration");
    }

    @Override
    public void loadConfig() {
        this.debug = plugin.getConfig().getBoolean("debug");
    }

    public long getBackupDuration() {
        return backupDuration;
    }

    public long getInitialBackupDuration() {
        return initialBackupDuration;
    }
}
