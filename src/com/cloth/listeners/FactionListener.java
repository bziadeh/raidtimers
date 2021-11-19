package com.cloth.listeners;

import com.cloth.config.Config;
import com.cloth.api.RaidApi;
import com.cloth.RaidTimers;
import com.cloth.raids.Raid;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;
import com.massivecraft.factions.event.LandUnclaimAllEvent;
import com.massivecraft.factions.event.LandUnclaimEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FactionListener implements Listener {

    private final RaidApi api = RaidTimers.getApi();
    private final Config config = RaidTimers.getLocalConfig();

    public FactionListener() {
        RaidTimers.getInstance().registerListener(this);
    }

    @EventHandler
    public void onUnclaim(LandUnclaimEvent event) {
        Faction faction = event.getFaction();
        Raid raid;
        if((raid = api.getRaidInProgress(faction)) != null && raid.getDefender().equals(faction)) {
            event.setCancelled(true);
            event.getfPlayer().sendMessage(config.CANNOT_UNCLAIM);
        }
    }

    @EventHandler
    public void onUnclaimAll(LandUnclaimAllEvent event) {
        Faction faction = event.getFaction();
        Raid raid;
        if((raid = api.getRaidInProgress(faction)) != null && raid.getDefender().equals(faction)) {
            event.setCancelled(true);
            event.getfPlayer().sendMessage(config.CANNOT_UNCLAIM);
        }
    }

    @EventHandler
    public void onFactionDisband(FactionDisbandEvent event) {
        final Faction faction = event.getFaction();
        if(api.getRaidInProgress(faction) != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(config.CANNOT_DISBAND);
        } else if(api.getShield(faction) != null) {
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