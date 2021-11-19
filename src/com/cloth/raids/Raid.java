package com.cloth.raids;

import com.cloth.config.Config;
import com.cloth.RaidTimers;
import com.cloth.inventory.GuiItem;
import com.cloth.inventory.RaidGui;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import lombok.Getter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DateFormat;
import java.util.Date;

public class Raid {

    @Getter private final RaidGui raidGui;
    @Getter private final long start;

    private final String defenderId;
    private final String attackerId;
    private final String date;

    private final Config config = RaidTimers.getLocalConfig();
    private long lastExplosion;

    public Raid(Faction attacker, Faction defender) {
        this(attacker.getId(), defender.getId(), System.currentTimeMillis(), System.currentTimeMillis());
    }

    public Raid(String attackerId, String defenderId, long lastExplosion, long start) {
        this.attackerId = attackerId;
        this.defenderId = defenderId;
        this.lastExplosion = lastExplosion;
        this.start = start;
        this.date = DateFormat.getDateTimeInstance().format(new Date(start));
        this.raidGui = new RaidGui(this, config.RAID_GUI, 45);
    }

    public ItemStack getItem() {
        long elapsed = (System.currentTimeMillis() - getLastExplosion());
        int defenderOnline = getDefender().getOnlinePlayers().size();
        int attackerOnline = getAttacker().getOnlinePlayers().size();

        ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
        FPlayer leader = getAttacker().getFPlayerLeader();
        String name = leader != null ? leader.getName() : "Cloth";
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(name);
        skull.setItemMeta(meta);

        GuiItem centerItem = new GuiItem(skull)
                .setName(config.RAID_ITEM_NAME)
                .setLore(config.RAID_ITEM_LORE)
                .replace("%date%", date)
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
