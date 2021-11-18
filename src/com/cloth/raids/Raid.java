package com.cloth.raids;

import com.cloth.Config;
import com.cloth.RaidTimers;
import com.cloth.inventory.GuiItem;
import com.cloth.inventory.RaidGui;
import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import lombok.Getter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.inventory.ItemStack;

public class Raid {

    @Getter private final RaidGui raidGui;
    private final String defenderId;
    private final String attackerId;
    private long lastExplosion;

    public Raid(Faction attacker, Faction defender) {
        this(attacker.getId(), defender.getId(), System.currentTimeMillis());
    }

    public Raid(String attackerId, String defenderId, long lastExplosion) {
        this.attackerId = attackerId;
        this.defenderId = defenderId;
        this.lastExplosion = lastExplosion;
        this.raidGui = new RaidGui(this, "Raid", 45);
    }

    public ItemStack getItem() {
        Config config = RaidTimers.getLocalConfig();

        // time elapsed since the last explosion
        long elapsed = (System.currentTimeMillis() - getLastExplosion());

        // getting online players for both factions
        int defenderOnline = getDefender().getOnlinePlayers().size();
        int attackerOnline = getAttacker().getOnlinePlayers().size();

        GuiItem centerItem = new GuiItem(XMaterial.TNT.parseMaterial())
                .setName(config.RAID_ITEM_NAME)
                .setLore(config.RAID_ITEM_LORE)
                .replace("%attacker%", getAttacker().getTag())
                .replace("%defender%", getDefender().getTag())
                .replace("%time%", getTimeElapsed(elapsed))
                .replace("%onlineAttackers%", String.valueOf(attackerOnline))
                .replace("%onlineDefenders%", String.valueOf(defenderOnline));
        return centerItem.getItem();
    }

    private String getTimeElapsed(long millis) {
        return DurationFormatUtils.formatDurationWords(millis, true, false);
    }

    public void setLastExplosion(long lastExplosion) {
        this.lastExplosion = lastExplosion;
    }

    public long getLastExplosion() {
        return lastExplosion;
    }

    public Faction getAttacker() {
        return Factions.getInstance().getFactionById(attackerId);
    }

    public Faction getDefender() {
        return Factions.getInstance().getFactionById(defenderId);
    }
}
