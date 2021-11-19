package com.cloth.command.raid;

import com.cloth.RaidTimers;
import com.cloth.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaidList implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can open the raid list gui.");
            return;
        }
        RaidTimers.getRaidListGui().open((Player) sender);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public int getRequiredLength() {
        return 1;
    }
}
