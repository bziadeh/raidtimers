package com.cloth.listeners;

import com.cloth.Config;
import com.cloth.RaidApi;
import com.cloth.RaidTimers;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    private final RaidApi api = RaidTimers.getApi();
    private final Config config = RaidTimers.getLocalConfig();

    public PlayerListener() {
        RaidTimers.getInstance().registerListener(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(config.PREVENT_BREAK.contains(event.getBlock().getType().name())) {

            final FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            Faction faction = player.getFaction();

            Raid context = api.getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                event.setCancelled(true);

                event.getPlayer().sendMessage(config.CANNOT_BREAK);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) {
            return;
        }

        if (XMaterial.matchXMaterial(event.getItem()).isOneOf(config.PREVENT_USE)) {
            FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            final Faction faction = player.getFaction();

            Raid context = api.getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                event.setCancelled(true);

                player.sendMessage(config.CANNOT_USE);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(config.PREVENT_PLACEMENT.contains(event.getBlock().getType().name())) {

            FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            final Faction faction = player.getFaction();

            Raid context = api.getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                Faction factionAt = Board.getInstance().getFactionAt(new FLocation(event.getBlockPlaced().getLocation()));

                if(factionAt.equals(faction)) {

                    event.setCancelled(true);

                    player.sendMessage(config.CANNOT_PLACE);
                }
            }
        }
    }
}
