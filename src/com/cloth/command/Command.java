package com.cloth.command;

import com.cloth.RaidTimers;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements CommandExecutor {

    private final List<SubCommand> subCommandList = new ArrayList<>();

    public Command(String command) {
        RaidTimers.getInstance().getCommand(command).setExecutor(this);
    }

    public void registerSubCommand(SubCommand command) {
        subCommandList.add(command);
    }

    public abstract void execute(CommandSender sender, String[] args, String label);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String label, @NotNull String[] args) {
        // Base command was executed...
        if(args.length == 0) {
            execute(sender, args, label);
            return false;
        }
        // Arguments were specified...
        final String subCommand = args[0];
        for(SubCommand command : subCommandList) {
            // Found the subcommand... execute it!
            if(command.getName().equalsIgnoreCase(subCommand)) {
                // Permission check...
                String permission;
                if(!(permission = command.getPermission()).isEmpty()) {
                    if(!sender.hasPermission(permission) && !sender.isOp()) {
                        sender.sendMessage("§cYou do not have permission.");
                        return false;
                    }
                }
                // Argument size check...
                if(args.length < command.getRequiredLength()) {
                    usageError(sender);
                    return false;
                }
                command.execute(sender, args, label);
                return false;
            }
        }
        usageError(sender);
        return false;
    }

    public void usageError(CommandSender sender) {
        sender.sendMessage("§cIncorrect usage. Please try again.");
    }
}
