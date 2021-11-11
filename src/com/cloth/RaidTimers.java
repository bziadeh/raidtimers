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

/**
 * @author Brennan Ziadeh
 * @author https://github.com/bziadeh
 */
public class RaidTimers extends JavaPlugin {

    // reference to the main class
    @Getter private static RaidTimers instance;
    private Config config;
    private RaidApi api;

    public void onEnable() {
        instance = this;
        config = Config.load(this);
        api = RaidApi.create();
        new RaidListener(this);
        new ActionListener(this);
        new CommandRaid(this);
        Bukkit.getScheduler().runTaskTimer(this, new ContextRunnable(), 0, 1200);
        Bukkit.getScheduler().runTaskTimer(this, new ShieldRunnable(), 0, 1200);
    }

    public void onDisable() {
        List<RaidContext> contextList = getApi().getContextList();
        for(int i = contextList.size() - 1; i >= 0; i--) {
            RaidContext context = getApi().getContextList().get(i);
            context.getRaidGui().destroy();
            contextList.remove(i);
        }
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

    /**
     * Gets the Config.
     *
     * @return the Config.
     */
    public Config getLocalConfig() {
        return config;
    }

    /**
     * Gets the RaidTimers API.
     *
     * @return the API.
     */
    public RaidApi getApi() {
        return api == null ? (api = RaidApi.create()) : api;
    }
}
