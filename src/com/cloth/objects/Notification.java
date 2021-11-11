package com.cloth.objects;

import com.massivecraft.factions.Faction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private List<String> messages = new ArrayList<>();
    private String title;
    private String subtitle;

    public Notification(String placeholder, String replacement, String title, String subtitle, List<String> messages) {
        this.title = title.replaceAll(placeholder, replacement);
        this.subtitle = subtitle.replaceAll(placeholder, replacement);
        for (String message : messages) {
            this.messages.add(message.replaceAll(placeholder, replacement));
        }
    }

    public Notification replace(String placeholder, String replacement) {
        title = title.replaceAll(placeholder, replacement);
        subtitle = subtitle.replaceAll(placeholder, replacement);
        List<String> messages = new ArrayList<>();
        this.messages.forEach(message -> messages.add(message.replaceAll(placeholder, replacement)));
        this.messages = messages;
        return this;
    }

    public void send(Faction faction) {
        faction.getOnlinePlayers().forEach(this::send);
    }

    public void send(Player player) {
        new Title(title, subtitle).send(player);
        messages.forEach(player::sendMessage);
    }
}
