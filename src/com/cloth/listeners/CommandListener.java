package com.cloth.listeners;

import com.cloth.RaidTimers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    public CommandListener() {
        RaidTimers.getInstance().registerListener(this);
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        List<String> commandsToPrevent = RaidTimers.getInstance().getLocalConfig().PREVENT_COMMANDS;
        if(commandsToPrevent == null)
            return;
        for(String blockedCommand : commandsToPrevent) {
            String command = event.getMessage()
                    .replaceFirst("factions", "f");
            if(blockedCommand.equalsIgnoreCase(command)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
