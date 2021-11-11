package com.cloth;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author Brennan Ziadeh
 * @author https://github.com/bziadeh
 */
public class Config {

    public final String RAID_START_TITLE, RAID_START_SUBTITLE;
    public final List<String> RAID_START;
    public final String RAID_DEFENSE_TITLE, RAID_DEFENSE_SUBTITLE;
    public final List<String> RAID_DEFENSE;
    public final String CANNOT_BREAK;
    public final String CANNOT_PLACE;
    public final String CANNOT_USE;
    public final int EXPLOSION_THRESHOLD;
    public final int SHIELD_DURATION;
    public final List<String> PREVENT_PLACEMENT;
    public final List<String> PREVENT_BREAK;
    public final List<String> PREVENT_USE;
    private static Config instance;
    private RaidTimers plugin;

    /**
     * Loads the data from the configuration file once.
     *
     * @param plugin the main class.
     */
    private Config(RaidTimers plugin) {
        // private constructor, singleton design pattern
        this.plugin = plugin;
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();
        RAID_START_TITLE = getString("raid-start-title");
        RAID_START_SUBTITLE = getString("raid-start-subtitle");
        RAID_START = getList("raid-start");
        RAID_DEFENSE_TITLE = getString("raid-defense-title");
        RAID_DEFENSE_SUBTITLE = getString("raid-defense-subtitle");
        RAID_DEFENSE = getList("raid-defense");
        CANNOT_BREAK = getString("cannot-break");
        CANNOT_PLACE = getString("cannot-place");
        CANNOT_USE = getString("cannot-use");
        EXPLOSION_THRESHOLD = minutesFromString(getString("explosion-threshold"));
        SHIELD_DURATION = minutesFromString(getString("shield-duration"));
        PREVENT_PLACEMENT = (List<String>) config.getList("prevent-placement");
        PREVENT_BREAK = (List<String>) config.getList("prevent-break");
        PREVENT_USE = (List<String>) config.getList("prevent-use");
    }

    /**
     * Creates and returns a single instance of this class.
     *
     * @param plugin the main class.
     * @return an instance of this class.
     */
    public static Config load(RaidTimers plugin) {
        if(instance == null) {
            return (instance = new Config(plugin));
        }
        return instance;
    }

    /**
     * Converts a time as a string into an integer in minutes.
     *
     * arg[0] = 2
     * arg[1] = hours
     *
     * if (arg[1] equals hours) return arg[0] * 60
     *
     * @param string the string being converted.
     * @return the amount of minutes.
     */
    private int minutesFromString(String string) {
        String[] seg = string.split(" ");
        String time = seg[1].toLowerCase();

        int value = Integer.parseInt(seg[0]);

        if(time.startsWith("hour"))
            value *= 60;

        if(time.startsWith("day"))
            value *= 60 * 24;

        return value;
    }

    /**
     * Gets a list from the configuration file and converts all color codes.
     *
     * @param path the path to the list.
     * @return the converted list.
     */
    private List<String> getList(String path) {
        List<String> list = (List<String>) plugin.getConfig().getList(path);
        for(int i = list.size() - 1; i >= 0; i--) {
            list.set(i, list.get(i).replaceAll("&", "§"));
        }
        return list;
    }

    /**
     * Gets a string from the configuration file and converts all color codes.
     *
     * @param path the path to the string.
     * @return the converted string.
     */
    private String getString(String path) {
        return plugin.getConfig().getString(path).replaceAll("&", "§");
    }
}
