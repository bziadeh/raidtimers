package com.cloth.command.raid;

import com.cloth.RaidTimers;
import com.cloth.api.RaidApi;
import com.cloth.command.SubCommand;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.command.CommandSender;

public class RaidStart implements SubCommand {

    private final RaidApi api = RaidTimers.getApi();

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        final String FACTION_NOT_FOUND = "§cUnable to find the specified faction: §7";
        final String FACTION_NOT_NORMAL = "§cYou cannot specify the system faction: §7";

        String attackerTag = args[1];
        String defenderTag = args[2];

        Faction attacker = Factions.getInstance().getByTag(attackerTag);
        if(attacker == null) {
            sender.sendMessage(FACTION_NOT_FOUND + attackerTag);
            return;
        }

        /*if(!attacker.isNormal()) {
            sender.sendMessage(FACTION_NOT_NORMAL + attacker.getTag());
            return;
        }*/

        Faction defender = Factions.getInstance().getByTag(defenderTag);
        if(defender == null) {
            sender.sendMessage(FACTION_NOT_FOUND + defenderTag);
            return;
        }

        /*if(!defender.isNormal()) {
            sender.sendMessage(FACTION_NOT_NORMAL + defender.getTag());
            return;
        }*/

        if(api.getRaidInProgress(attacker) != null || api.getRaidInProgress(defender) != null) {
            sender.sendMessage("§cOne or more of the specified factions are already in an active raid.");
            return;
        }

        api.setRaidInProgress(attacker, defender);
        sender.sendMessage("§aStarted a raid between: §7" + attacker.getTag() + "§a and §7" + defender.getTag());
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getPermission() {
        return "raidtimers.admin";
    }

    @Override
    public int getRequiredLength() {
        return 3;
    }
}
