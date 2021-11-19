package com.cloth.command.raid;

import com.cloth.api.RaidApi;
import com.cloth.RaidTimers;
import com.cloth.command.Command;
import com.cloth.config.Config;
import com.cloth.inventory.RaidListGui;
import com.cloth.raids.Raid;
import com.cloth.shield.Shield;
import com.cryptomorin.xseries.XSound;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaidCommand extends Command {

    private final RaidApi api = RaidTimers.getApi();
    private final Config config = RaidTimers.getLocalConfig();

    public RaidCommand(String command) {
        super(command);

        // register all sub commands here...
        registerSubCommand(new RaidHelp());
        registerSubCommand(new RaidStart());
        registerSubCommand(new RaidStop());
        registerSubCommand(new RaidReload());
        registerSubCommand(new RaidList());
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if(!(sender instanceof Player)) {
            usageError(sender);
            return;
        }

        final Player player = (Player) sender;
        final RaidListGui gui = RaidTimers.getRaidListGui();

        Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
        if(faction == null || !faction.isNormal()) {
            gui.open(player);
            return;
        }

        Raid raid = api.getRaidInProgress(faction);
        if(raid != null) {
            raid.getRaidGui().open(player);
            XSound.BLOCK_NOTE_BLOCK_PLING.play(player);
            return;
        }

        Shield shield;
        if((shield = api.getShield(faction)) != null) {
            player.sendMessage(config.SHIELD.replaceAll("%time%", shield.getFormattedTime()));
            return;
        }
        gui.open(player);
    }
}
