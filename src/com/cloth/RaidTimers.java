package com.cloth;

import com.cloth.api.RaidApi;
import com.cloth.command.raid.RaidCommand;
import com.cloth.command.Command;
import com.cloth.config.Config;
import com.cloth.framework.CustomPlugin;
import com.cloth.inventory.RaidListGui;
import com.cloth.listeners.FactionListener;
import com.cloth.raids.Raid;
import com.cloth.listeners.CommandListener;
import com.cloth.listeners.PlayerListener;
import com.cloth.listeners.RaidListener;
import com.cloth.raids.RaidRunnable;
import com.cloth.shield.ShieldExpansion;
import com.cloth.shield.ShieldRunnable;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;

public class RaidTimers extends CustomPlugin {

    @Getter private static Config localConfig;
    @Getter private static RaidApi api;
    @Getter private static RaidListGui raidListGui;

    @Override
    public void onPluginLoad() {
        localConfig = Config.load();
        api = RaidApi.create();

        new RaidListener();
        new PlayerListener();
        new CommandListener();
        new FactionListener();

        // todo: time remaining feature?
        // todo: rewards? kill other members to get items?
        new RaidCommand("raid");

        Bukkit.getScheduler().runTaskTimer(this, new RaidRunnable(), 0, 20);
        Bukkit.getScheduler().runTaskTimer(this, new ShieldRunnable(), 0, 20);

        // Create our GUI displaying the list of ALL active raids on the server.
        raidListGui = new RaidListGui(localConfig.RAID_LIST_GUI, 54);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ShieldExpansion().register();
        }
    }

    public void onDisable() {
        final List<Raid> raids = api.getRaidList();

        // cleanup before loading raids from flat-file
        // remove them from memory, but DO NOT unsave them here
        for(int i = raids.size() - 1; i >= 0; i--) {
            Raid raid = raids.get(i);
            raid.getRaidGui().destroy();
            raids.remove(i);
        }

        raidListGui.destroy();
        System.out.println("Removed raids from memory. These will be recreated when the server starts.");
    }
}
