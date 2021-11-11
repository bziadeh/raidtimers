package com.cloth.listeners;

import com.cloth.RaidTimers;
import com.cloth.context.RaidContext;
import com.massivecraft.factions.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.SpawnEgg;

public class ActionListener implements Listener {

    private final RaidTimers plugin;

    public ActionListener(RaidTimers instance) {
        this.plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(RaidTimers.getInstance().getLocalConfig().PREVENT_BREAK.contains(event.getBlock().getType().name())) {

            final FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            Faction faction = player.getFaction();

            RaidContext context = RaidTimers.getInstance().getApi().getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                event.setCancelled(true);

                event.getPlayer().sendMessage(RaidTimers.getInstance().getLocalConfig().CANNOT_BREAK);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) {
            return;
        }

        if(RaidTimers.getInstance().getLocalConfig().PREVENT_USE.contains(event.getItem().getType().name())) {

            SpawnEgg egg = (SpawnEgg) event.getItem().getData();

            if(egg.getSpawnedType() != EntityType.CREEPER) {
                return;
            }

            FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            final Faction faction = player.getFaction();

            RaidContext context = RaidTimers.getInstance().getApi().getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                event.setCancelled(true);

                player.sendMessage(RaidTimers.getInstance().getLocalConfig().CANNOT_USE);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(RaidTimers.getInstance().getLocalConfig().PREVENT_PLACEMENT.contains(event.getBlock().getType().name())) {

            FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());

            if(!player.hasFaction()) {
                return;
            }

            final Faction faction = player.getFaction();

            RaidContext context = RaidTimers.getInstance().getApi().getRaidInProgress(faction);

            if(context != null && context.getDefender().equals(faction)) {

                Faction factionAt = Board.getInstance().getFactionAt(new FLocation(event.getBlockPlaced().getLocation()));

                if(factionAt.equals(faction)) {

                    event.setCancelled(true);

                    player.sendMessage(RaidTimers.getInstance().getLocalConfig().CANNOT_PLACE);
                }
            }
        }
    }
}
