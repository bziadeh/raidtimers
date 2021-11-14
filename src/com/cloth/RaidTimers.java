package com.cloth;

import com.cloth.command.CommandRaid;
import com.cloth.context.RaidContext;
import com.cloth.listeners.ActionListener;
import com.cloth.listeners.RaidListener;
import com.cloth.context.ContextRunnable;
import com.cloth.shield.ShieldRunnable;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RaidTimers extends JavaPlugin {

    @Getter private static RaidTimers instance;
    @Getter private Config localConfig;
    @Getter private RaidApi api;

    /**
     * Called when this plugin is enabled. All instantiated classes
     * must go here instead of the constructor to ensure dependencies are loaded.
     */
    public void onEnable() {
        instance = this;
        localConfig = Config.load(this);
        api = RaidApi.create();

        new RaidListener(this);
        new ActionListener(this);
        new CommandRaid(this);

        Bukkit.getScheduler().runTaskTimer(this, new ContextRunnable(), 0, 1200);
        Bukkit.getScheduler().runTaskTimer(this, new ShieldRunnable(), 0, 1200);

        // todo: prevent faction disband during raids...
        // todo: time remaining feature?
        // todo: rewards? kill other members to get items?

    }

    /**
     * Called when this plugin is disabled. Do any necessary cleanup
     * here to ensure there aren't memory leaks or unneeded tasks still running.
     */
    public void onDisable() {
        final List<RaidContext> raids = api.getContextList();
        // cleanup before loading raids from flat-file
        // remove them from memory, but DO NOT unsave them here
        for(int i = raids.size() - 1; i >= 0; i--) {
            RaidContext context = raids.get(i);
            context.getRaidGui().destroy();
            raids.remove(i);
        }
        System.out.println("Removed raids from memory. These will be recreated when the server starts.");
    }

    /**
     * Registers the specified listener.
     *
     * @param listener the listener being registered.
     */
    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }
}
