package com.cloth.objects;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Title {

    private PacketPlayOutTitle title;
    private PacketPlayOutTitle subtitle;

    public Title(String text) {
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), 20, 40, 20);
        this.title = title;
    }

    public Title(String text, String subtext) {
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), 20, 40, 20);
        this.title = title;

        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtext + "\"}"), 20, 40, 20);
        this.subtitle = subtitle;
    }

    public void send(Player p) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(subtitle);
    }
}