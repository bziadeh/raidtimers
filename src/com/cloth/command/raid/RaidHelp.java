package com.cloth.command.raid;

import com.cloth.command.SubCommand;
import org.bukkit.command.CommandSender;

public class RaidHelp implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        String prefix = "/" + label + " ";
        sender.sendMessage("§7Displaying commands");
        sender.sendMessage(prefix + "help");
        sender.sendMessage(prefix + "reload");
        sender.sendMessage(prefix + "start <attacker> <defender>");
        sender.sendMessage(prefix + "stop <faction>");
        sender.sendMessage(prefix + "list");
    }

    @Override
    public String getName() {
        return "help";
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
