package com.cloth.listeners;

import com.cloth.RaidTimers;
import com.cloth.config.Config;
import com.cloth.raids.Raid;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    private final Config config = RaidTimers.getLocalConfig();

    public CommandListener() {
        RaidTimers.getInstance().registerListener(this);
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
        if(faction == null || !faction.isNormal()) {
            return;
        }
        Raid raid;
        if((raid = RaidTimers.getApi().getRaidInProgress(faction)) != null) {
            if(raid.getDefender().equals(faction)) {
                List<String> blocked = config.PREVENT_COMMANDS;
                String message = event.getMessage().substring(1).replaceFirst("factions", "f");
                if(blocked.contains(message)) {
                    event.setCancelled(true);
                    player.sendMessage(config.CANNOT_COMMAND);
                }
            }
        }
    }
}
