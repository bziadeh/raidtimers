package com.cloth.events;

import com.cloth.RaidTimers;
import com.cloth.raids.Raid;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RaidStartEvent extends Event {

    @Getter private final Raid raid;
    private final static HandlerList HANDLER_LIST = new HandlerList();

    public RaidStartEvent(Raid raid) {
        this.raid = raid;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}