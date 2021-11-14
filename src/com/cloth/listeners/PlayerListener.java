package com.cloth.listeners;

import com.cloth.RaidTimers;
import com.cloth.context.RaidContext;
import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    private final RaidTimers plugin = RaidTimers.getInstance();

    public PlayerListener() {
        plugin.registerListener(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(plugin.getLocalConfig().PREVENT_BREAK.contains(event.getBlock().getType().name())) {

            final FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            Faction faction = player.getFaction();

            RaidContext context = plugin.getApi().getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                event.setCancelled(true);

                event.getPlayer().sendMessage(plugin.getLocalConfig().CANNOT_BREAK);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) {
            return;
        }

        if (XMaterial.matchXMaterial(event.getItem()).isOneOf(plugin.getLocalConfig().PREVENT_USE)) {
            FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            final Faction faction = player.getFaction();

            RaidContext context = plugin.getApi().getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                event.setCancelled(true);

                player.sendMessage(plugin.getLocalConfig().CANNOT_USE);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(plugin.getLocalConfig().PREVENT_PLACEMENT.contains(event.getBlock().getType().name())) {

            FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            final Faction faction = player.getFaction();

            RaidContext context = plugin.getApi().getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                Faction factionAt = Board.getInstance().getFactionAt(new FLocation(event.getBlockPlaced().getLocation()));

                if(factionAt.equals(faction)) {

                    event.setCancelled(true);

                    player.sendMessage(plugin.getLocalConfig().CANNOT_PLACE);
                }
            }
        }
    }
}
