package com.cloth;

import com.cloth.context.RaidContext;
import com.cloth.objects.Notification;
import com.cloth.shield.Shield;
import com.cryptomorin.xseries.XSound;
import com.massivecraft.factions.Faction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brennan Ziadeh
 * @author https://github.com/bziadeh
 */
public class RaidApi {

    private static RaidApi instance;

    @Getter private final List<RaidContext> contextList = new ArrayList<>();
    @Getter private final List<Shield> shieldList = new ArrayList<>();

    private File file;
    private FileConfiguration config;

    private RaidApi() {
        // private constructor, singleton design pattern
        createConfig();
    }

    /**
     * Creates and returns single instance of this class.
     *
     * @return the RaidApi.
     */
    public static RaidApi create() {
        if(instance == null) {
            return (instance = new RaidApi());
        }
        return instance;
    }

    /**
     * Creates a raids.yml file if one does not exist. If one does,
     * loads all raids stored in the file.
     */
    private void createConfig() {
        RaidTimers plugin = RaidTimers.getInstance();
        // create new config?
        file = new File(plugin.getDataFolder(), "raids.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        loadConfig();
    }

    /**
     * Loading all raids stored in raids.yml
     */
    private void loadConfig() {
        ConfigurationSection raids = config.getConfigurationSection("raids");
        for(String attackerId : raids.getKeys(false)) {
            String defenderId = config.getString("raids." + attackerId + ".attacking");
            long lastExplosion = config.getLong("raids." + attackerId + ".lastExplosion");
            contextList.add(new RaidContext(attackerId, defenderId, lastExplosion));
        }
    }

    /**
     * Adds a raid to our list of active raids.
     * Saves this to the config "raids.yml"
     *
     * @param raid the raid being added.
     */
    public void addRaid(RaidContext raid)  {
        contextList.add(raid);
        saveToConfig(raid);
    }

    /**
     * Removes this raid from our list of active raids.
     * Deletes it from the config "raids.yml"
     *
     * @param raid the raid being removed.
     */
    public void removeRaid(RaidContext raid) {
        raid.getRaidGui().destroy();
        contextList.remove(raid);
        deleteFromConfig(raid);
    }

    /**
     * Saves this raid to "raids.yml" file.
     *
     * @param raid the raid being saved.
     */
    private void saveToConfig(RaidContext raid) {
        Bukkit.getScheduler().runTaskAsynchronously(RaidTimers.getInstance(), () -> {
            String id = raid.getAttacker().getId();
            config.set(id + ".raiding", raid.getDefender().getId());
            config.set(id + ".lastExplosion", raid.getLastExplosion());
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deletes this raid from "raids.yml" file.
     *
     * @param raid the raid being deleted.
     */
    private void deleteFromConfig(RaidContext raid) {
        Bukkit.getScheduler().runTaskAsynchronously(RaidTimers.getInstance(), () -> {
            String id = raid.getAttacker().getId();

            // does the config contain this raid context?
            if(!config.contains(id + ".raiding"))
                return;

            config.set(id + ".raiding", null);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initiates a raid between the two factions. Creates a new RaidContext
     * object, and sends a notification to both factions.
     *
     * @param defender the defending faction.
     * @param attacker the attacking faction.
     */
    public void setRaidInProgress(Faction defender, Faction attacker) {
        Config config = RaidTimers.getInstance().getLocalConfig();
        new Notification("%faction%", defender.getTag(),
                config.RAID_START_TITLE,
                config.RAID_START_SUBTITLE,
                config.RAID_START)
                .replace("%online%", String.valueOf(defender.getOnlinePlayers().size()))
                .send(attacker);
        new Notification("%faction%", attacker.getTag(),
                config.RAID_DEFENSE_TITLE,
                config.RAID_DEFENSE_SUBTITLE,
                config.RAID_DEFENSE)
                .replace("%online%", String.valueOf(attacker.getOnlinePlayers().size()))
                .send(defender);
        RaidContext context = new RaidContext(defender, attacker);
        addRaid(context);
        // Play a sound notification that the raid has started...
        XSound raidStartSound = XSound.ENTITY_BLAZE_DEATH;
        attacker.getOnlinePlayers().forEach(raidStartSound::play);
        defender.getOnlinePlayers().forEach(raidStartSound::play);
    }

    /**
     * Gets the RaidContext for the specified faction. Null if none.
     *
     * @param faction the faction being requested.
     * @return the RaidContext.
     */
    public RaidContext getRaidInProgress(Faction faction) {
        return contextList.stream().filter(context -> context.getAttacker().equals(faction) || context.getDefender().equals(faction)).findFirst().orElse(null);
    }

    /**
     * Gives the specified faction a shield.
     *
     * @param faction the faction being given a shield.
     */
    public void setShield(Faction faction) {
        shieldList.add(new Shield(faction));
    }

    /**
     * Checks if the specified faction has a shield.
     *
     * @param faction the faction being checked.
     * @return whether it has an active shield.
     */
    public boolean hasShield(Faction faction) {
        for(Shield shield : shieldList) {
            if(shield.getFaction().equals(faction)) {
                return true;
            }
        }
        return false;
    }
}