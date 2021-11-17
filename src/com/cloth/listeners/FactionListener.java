package com.cloth.listeners;

import com.cloth.Config;
import com.cloth.RaidApi;
import com.cloth.RaidTimers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FactionListener implements Listener {

    private final RaidApi api = RaidTimers.getApi();
    private final Config config = RaidTimers.getLocalConfig();

    public FactionListener() {
        RaidTimers.getInstance().registerListener(this);
    }

    @EventHandler
    public void onFactionDisband(FactionDisbandEvent event) {
        final Faction faction = event.getFaction();
        if(api.getRaidInProgress(faction) != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(config.CANNOT_DISBAND);
        } else if(api.hasShield(faction)) {
            // cleanup. the faction was disbanded, remove from memory
            api.removeShield(faction);
        }
    }

    @EventHandler
    public void onFactionLeave(FPlayerLeaveEvent event) {
        final Faction faction = event.getFaction();
        if(faction.isNormal() && api.getRaidInProgress(faction) != null) {
            event.setCancelled(true);
            event.getfPlayer().sendMessage(config.CANNOT_LEAVE);
        }
    }
}
