package com.cloth.shield;

import com.massivecraft.factions.Faction;
import lombok.Getter;

public class Shield {

    @Getter private final Faction faction;
    @Getter private final long startTime = System.currentTimeMillis();

    public Shield(Faction faction) {
        this.faction = faction;
    }
}
