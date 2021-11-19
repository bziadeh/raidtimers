package com.cloth.inventory;

import com.cryptomorin.xseries.XEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public GuiItem setLore(List<String> lore) {
        final List<String> translated = new ArrayList<>();
        for(String line : lore)
            translated.add(ChatColor.translateAlternateColorCodes('&', line));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(translated);
        item.setItemMeta(meta);
        return this;
    }

    public GuiItem replace(String placeholder, String replacement) {
        if(!item.hasItemMeta()) {
            return this;
        }

        // update the lore placeholders
        if(item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            List<String> updated = new ArrayList<>();
            for(String line : lore) {
                updated.add(line.replaceAll(placeholder, replacement));
            }
            setLore(updated);
        }

        // update the name placeholders
        if(item.getItemMeta().hasDisplayName()) {
            setName(item.getItemMeta().getDisplayName().replaceAll(placeholder, replacement));
        }

        return this;
    }

    public GuiItem fakeEnchantment() {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(XEnchantment.DURABILITY.parseEnchantment(), 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

}
