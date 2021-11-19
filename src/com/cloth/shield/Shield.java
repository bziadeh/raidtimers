package com.cloth.shield;

import com.cloth.RaidTimers;
import com.massivecraft.factions.Faction;
import lombok.Getter;

public class Shield {

    @Getter private final Faction faction;
    @Getter private final long startTime = System.currentTimeMillis();

    public Shield(Faction faction) {
        this.faction = faction;
    }

    public String getFormattedTime() {
        int duration = RaidTimers.getLocalConfig().SHIELD_DURATION * 1000 * 60;
        long elapsed = (System.currentTimeMillis() - getStartTime());
        return getTimeLeft(duration - elapsed);
    }

    private String getTimeLeft(long difference) {
        long sec = difference / 1000 % 60;
        long min  = difference / (60 * 1000) % 60;
        String minutes = format(min, "minutes");
        String seconds = format(sec, "seconds");
        if(minutes.isEmpty() && seconds.isEmpty()) {
            return "0 seconds. Removing shield.";
        }
        return String.format("%s %s", minutes, seconds);
    }

    private String format(long amount, String suffix) {
        String formatted = "";
        if(amount < 1) {
            return formatted;
        } else if(amount < 2) {
            formatted = String.format("%d %s", amount, suffix.substring(0, suffix.length() - 1));
        } else {
            formatted = String.format("%d %s", amount, suffix);
        }
        return formatted;
    }
}
