package com.cloth;

import com.cloth.framework.CustomPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author Brennan Ziadeh
 * @author https://github.com/bziadeh
 */
public class Config {

    private final CustomPlugin plugin = RaidTimers.getInstance();
    private static Config instance;

    // All values in the configuration file loaded ONCE.
    public final String RAID_START_TITLE, RAID_START_SUBTITLE;
    public final List<String> RAID_START;
    public final String RAID_DEFENSE_TITLE, RAID_DEFENSE_SUBTITLE;
    public final List<String> RAID_DEFENSE;

    public final String CANNOT_BREAK;
    public final String CANNOT_PLACE;
    public final String CANNOT_USE;
    public final String CANNOT_EXECUTE;
    public final String CANNOT_DISBAND;
    public final String CANNOT_LEAVE;

    public final int EXPLOSION_THRESHOLD;
    public final int SHIELD_DURATION;

    public final List<String> PREVENT_PLACEMENT;
    public final List<String> PREVENT_BREAK;
    public final List<String> PREVENT_USE;
    public final List<String> PREVENT_COMMANDS;

    public final String RAID_ITEM_NAME;
    public final List<String> RAID_ITEM_LORE;

    private Config() {
        // private constructor, singleton design pattern
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();

        // raid notification messages
        RAID_START_TITLE = getString("raid-start-title");
        RAID_START_SUBTITLE = getString("raid-start-subtitle");
        RAID_START = getList("raid-start");
        RAID_DEFENSE_TITLE = getString("raid-defense-title");
        RAID_DEFENSE_SUBTITLE = getString("raid-defense-subtitle");
        RAID_DEFENSE = getList("raid-defense");

        // general messages
        CANNOT_BREAK = getString("cannot-break");
        CANNOT_PLACE = getString("cannot-place");
        CANNOT_USE = getString("cannot-use");
        CANNOT_EXECUTE = getString("cannot-execute");
        CANNOT_DISBAND = getString("cannot-disband");
        CANNOT_LEAVE = getString("cannot-leave");

        // raid settings
        EXPLOSION_THRESHOLD = minutesFromString(getString("explosion-threshold"));
        SHIELD_DURATION = minutesFromString(getString("shield-duration"));

        // lists of blocks, items, commands, etc.
        PREVENT_PLACEMENT = (List<String>) config.getList("prevent-placement");
        PREVENT_BREAK = (List<String>) config.getList("prevent-break");
        PREVENT_USE = (List<String>) config.getList("prevent-use");
        PREVENT_COMMANDS = (List<String>) config.getList("prevent-commands");

        // inventory items
        RAID_ITEM_NAME = getString("raid-item.name");
        RAID_ITEM_LORE = getList("raid-item.lore");
    }

    /**
     * Creates and returns a single instance of this class.
     *
     * @return an instance of this class.
     */
    public static Config load() {
        if(instance == null) {
            return (instance = new Config());
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
