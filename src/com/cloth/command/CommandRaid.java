package com.cloth.command;

import com.cloth.RaidTimers;
import com.cloth.context.RaidContext;
import com.cryptomorin.xseries.XSound;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRaid implements CommandExecutor {

    public CommandRaid() {
        RaidTimers.getInstance().getCommand("raid").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false;
        }

        final Player player = (Player) sender;
        if(args.length == 0 || !player.hasPermission("raidtimers.admin")) {
            openGui(player);
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            // todo: reload the plugin.
            return true;
        }

        openGui(player);
        return false;
    }

    private void openGui(Player player) {
        Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
        if(faction == null || !faction.isNormal())
            return;

        RaidContext context;
        if((context = RaidTimers.getInstance().getApi().getRaidInProgress(faction)) != null) {
            context.getRaidGui().open(player);
            XSound.BLOCK_NOTE_BLOCK_PLING.play(player);
        }
    }
}
