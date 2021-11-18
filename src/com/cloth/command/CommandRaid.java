package com.cloth.command;

import com.cloth.RaidApi;
import com.cloth.RaidTimers;
import com.cloth.events.RaidEndEvent;
import com.cloth.inventory.RaidListGui;
import com.cloth.raids.Raid;
import com.cryptomorin.xseries.XSound;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRaid implements CommandExecutor {

    private final RaidApi api = RaidTimers.getApi();

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
        RaidListGui gui = RaidTimers.getRaidListGui();

        Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
        if(faction == null || !faction.isNormal()) {
            gui.open(player);
            return;
        }

        Raid raid = api.getRaidInProgress(faction);
        if(raid == null) {
            if(api.hasShield(faction)) {
                // message shield info
                return;
            }
            gui.open(player);
            return;
        }

        raid.getRaidGui().open(player);
        XSound.BLOCK_NOTE_BLOCK_PLING.play(player);
    }
}
