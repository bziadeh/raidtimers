package com.cloth.listeners;

import com.cloth.config.Config;
import com.cloth.api.RaidApi;
import com.cloth.RaidTimers;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        if(event.getItem() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        for(String item : config.PREVENT_USE) {

            XMaterial material = XMaterial.matchXMaterial(item).orElse(null);

            if(material == null) {
                System.out.println("[Warning] Null material name in prevent-use list: " + item);

                continue;
            }

            if(event.getItem().getType() == material.parseMaterial()) {
                FPlayer player = FPlayers.getInstance().getByPlayer(event.getPlayer());
                Player normal = player.getPlayer();

                if(!player.hasFaction()) {
                    return;
                }

                final Faction faction = player.getFaction();

                Raid context = api.getRaidInProgress(faction);

                if(context != null && context.getDefender().equals(faction)) {
                    event.setCancelled(true);
                    normal.sendMessage(config.CANNOT_USE);
                    return;
                }
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
