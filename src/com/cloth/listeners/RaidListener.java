package com.cloth.listeners;

import com.cloth.RaidTimers;
import com.cloth.framework.CustomPlugin;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XBlock;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class RaidListener implements Listener {

    private final CustomPlugin plugin = RaidTimers.getInstance();

    public RaidListener() {
        plugin.registerListener(this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // todo: predict who shot the TNT and send them a message to claim their land...
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if(event.getItem().getType() == Material.TNT && event.getBlock().getType() == Material.DISPENSER) {
            final Block block = event.getBlock();
            final Faction faction = Board.getInstance().getFactionAt(new FLocation(block));

            if(!faction.isNormal()) {
                return;
            }
            event.setCancelled(true);

            BlockFace face = XBlock.getDirection(block);
            Block front = block.getRelative(face); // get block in front of dispenser
            Location location = front.getLocation().clone();

            TNTPrimed tnt = block.getWorld().spawn(location.add(0.5, 0.5, 0.5), TNTPrimed.class);
            tnt.setMetadata("faction", new FixedMetadataValue(plugin, faction.getTag()));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.getEntityType() == EntityType.PRIMED_TNT) {
            Faction faction = Board.getInstance().getFactionAt(new FLocation(event.getLocation()));

            if(!faction.isNormal()) {
                return;
            }

            if(RaidTimers.getApi().hasShield(faction)) {
                event.setCancelled(true);
                return;
            }

            final TNTPrimed tnt = (TNTPrimed) event.getEntity();

            if(!tnt.hasMetadata("faction")) {
                return;
            }

            Faction attacker = Factions.getInstance().getByTag(tnt.getMetadata("faction").get(0).asString());

            if(attacker == null || !attacker.isNormal() || !faction.isNormal() || attacker.equals(faction)) {
                return;
            }

            final Raid context = RaidTimers.getApi().getRaidInProgress(faction);

            if(context != null) {

                if(!context.getAttacker().equals(attacker)) {
                    // The faction is already being raided by someone else!
                    return;
                }

                context.setLastExplosion(System.currentTimeMillis());
                return;
            }

            RaidTimers.getApi().setRaidInProgress(attacker, faction);
        }
    }

}
