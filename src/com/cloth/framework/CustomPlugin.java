package com.cloth.framework;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CustomPlugin extends JavaPlugin {

    @Getter private static CustomPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        onPluginLoad();
    }

    public abstract void onPluginLoad();

    /**
     * Registers the specified listener.
     *
     * @param listener the listener being registered.
     */
    public void registerListener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }
}
