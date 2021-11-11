package com.cloth.listeners;

import com.cloth.RaidTimers;
import com.cloth.context.RaidContext;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class RaidListener implements Listener {

    private final RaidTimers plugin;

    public RaidListener(RaidTimers instance) {
        this.plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

            event.setCancelled(true);

            TNTPrimed tnt = block.getWorld().spawn(block.getLocation(), TNTPrimed.class);

            tnt.setMetadata("faction", new FixedMetadataValue(plugin, faction.getTag()));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.getEntityType() == EntityType.PRIMED_TNT) {
            Faction faction = Board.getInstance().getFactionAt(new FLocation(event.getLocation()));

            if(RaidTimers.getInstance().getApi().hasShield(faction)) {
                event.setCancelled(true);

                return;
            }

            final TNTPrimed tnt = (TNTPrimed) event.getEntity();

            if(!tnt.hasMetadata("faction")) { // does this fix the error in console?
                return;
            }

            Faction attacker = Factions.getInstance().getByTag(tnt.getMetadata("faction").get(0).asString());

            if(attacker == null || !attacker.isNormal() || !faction.isNormal() || attacker.equals(faction)) {
                return;
            }

            final RaidContext context = RaidTimers.getInstance().getApi().getRaidInProgress(faction);

            if(context != null) {

                if(!context.getAttacker().equals(attacker)) {
                    // The faction is already being raided by someone else!

                    return;
                }

                context.setLastExplosion(System.currentTimeMillis());
                return;
            }

            RaidTimers.getInstance().getApi().setRaidInProgress(faction, attacker);
        }
    }
}
