package com.cloth.command.raid;

import com.cloth.command.SubCommand;
import org.bukkit.command.CommandSender;

public class RaidReload implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args, String label) {

    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "raidtimers.admin";
    }

    @Override
    public int getRequiredLength() {
        return 1;
    }
}
