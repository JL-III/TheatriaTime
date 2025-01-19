package com.playtheatria.theatriaTime.managers;

import com.playtheatria.jliii.generalutils.config.AbstractConfigManager;
import com.playtheatria.theatriaTime.TheatriaTime;

public class ConfigManager extends AbstractConfigManager<TheatriaTime> {
    private final long backupDuration;
    private final long initialBackupDuration;

    public ConfigManager(TheatriaTime plugin) {
        super(plugin);
        this.backupDuration = plugin != null ? plugin.getConfig().getLong("backup-duration") : 600;
        this.initialBackupDuration = plugin != null ? plugin.getConfig().getLong("initial-backup-duration") : 300;
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
