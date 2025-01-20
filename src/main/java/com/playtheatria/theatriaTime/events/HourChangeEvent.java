package com.playtheatria.theatriaTime.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class HourChangeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalDateTime lastHour;
    private final LocalDateTime now;

    public HourChangeEvent(LocalDateTime lastResetHour, LocalDateTime now) {
        this.lastHour = lastResetHour;
        this.now = now;
    }

    public LocalDateTime getLastHour() {
        return lastHour;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
