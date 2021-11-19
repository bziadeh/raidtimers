package com.cloth.shield;

import com.cloth.RaidTimers;
import com.cloth.api.RaidApi;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShieldExpansion extends PlaceholderExpansion {

    private final RaidApi api;

    public ShieldExpansion() {
        api = RaidTimers.getApi();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.equalsIgnoreCase("shield")) {
            FPlayer fplayer = FPlayers.getInstance().getByOfflinePlayer(player);
            Faction faction = fplayer.getFaction();

            if(faction == null || !faction.isNormal()) {
                return null;
            }

            Shield shield = api.getShield(faction);
            if(shield != null) {
                return shield.getFormattedTime();
            }
        }
        return null;
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "Factions";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "raidtimers";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Cloth";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }
}
