package com.cloth.command;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    void execute(CommandSender sender, String[] args, String label);

    String getName();

    String getPermission();

    int getRequiredLength();
}
