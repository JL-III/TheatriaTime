package com.playtheatria.theatriaTime.commands;

import com.playtheatria.jliii.generalutils.events.time.HourChangeEvent;
import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import com.playtheatria.jliii.generalutils.utils.TimeUtils;
import com.playtheatria.theatriaTime.TheatriaTime;
import com.playtheatria.theatriaTime.database.ResetTime;
import com.playtheatria.theatriaTime.managers.ResetTimeManager;
import com.playtheatria.theatriaTime.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public class ResetTimeCommand implements CommandExecutor, TabCompleter {
    private final ResetTimeManager resetTimeManager;
    private final ConfigManager theatriaTimeConfigManager;
    private final CustomLogger<TheatriaTime, ConfigManager> customLogger;
    private final String ADMIN_PERMISSION = "theatria.time.admin";

    public ResetTimeCommand(
            ResetTimeManager resetTimeManager,
            ConfigManager theatriaTimeConfigManager,
            CustomLogger<TheatriaTime, ConfigManager> customLogger
    ) {
        this.resetTimeManager = resetTimeManager;
        this.theatriaTimeConfigManager = theatriaTimeConfigManager;
        this.customLogger = customLogger;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) return true;
        switch (args.length) {
            case 1 -> {
                switch (args[0].toLowerCase()) {
                    case "reload-config" -> {
                        customLogger.sendFormattedMessage("Reloading config", sender);
                        theatriaTimeConfigManager.reloadConfig();
                        return true;
                    }
                    case "reset-time" -> {
                        customLogger.sendFormattedMessage(String.format("Reset Time: %s", resetTimeManager.getResetTime().getLastResetHour()), sender);
                        customLogger.sendFormattedMessage(String.format("Next Reset: %s", resetTimeManager.getResetTime().getNextResetHour()), sender);
                        return true;
                    }
                    // We intentionally set the ResetTime to an expired amount to leverage detection and trigger a reset.
                    case "reset-time-trigger" -> {
                        resetTimeManager.setResetTime(new ResetTime(LocalDateTime.now(TimeUtils.timeZone).minusDays(2)));
                        return true;
                    }
                }
            }
            case 2 -> {
                if (!sender.hasPermission(ADMIN_PERMISSION)) return true;
                switch (args[0].toLowerCase()) {
                    case "graduate" -> {
                        switch (args[1]) {
                            case "hour" -> {
                                Bukkit.getPluginManager().callEvent(
                                        new HourChangeEvent(resetTimeManager.getResetTime().getLastResetHour(), LocalDateTime.now(TimeUtils.timeZone))
                                );
                            }
                            default -> {
                                sender.sendMessage("Not a valid time-event");
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) return List.of();
        return List.of(
                "graduate",
                "reset-time",
                "reset-time-trigger"
        );
    }
}
