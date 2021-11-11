package com.cloth.context;

import com.cloth.inventory.RaidGui;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import lombok.Getter;

public class RaidContext {

    @Getter private final RaidGui raidGui;
    private final String defenderId;
    private final String attackerId;
    private long lastExplosion;

    public RaidContext(Faction defender, Faction attacker) {
        this(defender.getId(), attacker.getId(), System.currentTimeMillis());
    }

    public RaidContext(String defenderId, String attackerId, long lastExplosion) {
        this.defenderId = defenderId;
        this.attackerId = attackerId;
        this.lastExplosion = lastExplosion;
        this.raidGui = new RaidGui(this);
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
