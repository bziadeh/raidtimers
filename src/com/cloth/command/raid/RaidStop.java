package com.cloth.command.raid;

import com.cloth.RaidTimers;
import com.cloth.api.RaidApi;
import com.cloth.command.SubCommand;
import com.cloth.raids.Raid;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.command.CommandSender;

public class RaidStop implements SubCommand {

    final RaidApi api = RaidTimers.getApi();

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        Faction faction = Factions.getInstance().getByTag(args[1]);

        Raid raid;
        if((raid = api.getRaidInProgress(faction)) != null) {
            api.removeRaid(raid);
            sender.sendMessage("§aSuccessfully stopped the raid.");
        } else {
            sender.sendMessage("§cThis faction is not in an active raid.");
        }
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getPermission() {
        return "raidtimers.admin";
    }

    @Override
    public int getRequiredLength() {
        return 2;
    }
}
