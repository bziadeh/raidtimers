package com.cloth.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiItem {

    private final ItemStack item;

    public GuiItem(Material material) {
        this.item = new ItemStack(material);
    }

    public GuiItem(ItemStack item) {
        this.item = item.clone();
    }

    public GuiItem setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }

    public GuiItem setLore(String... lore) {
        final List<String> translated = new ArrayList<>();
        for(String line : lore)
            translated.add(ChatColor.translateAlternateColorCodes('&', line));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(translated);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

}
