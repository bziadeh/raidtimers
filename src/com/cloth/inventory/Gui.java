package com.cloth.inventory;

import org.bukkit.event.Listener;

public interface Gui extends Listener {

    void updateBorder();
    void updateItems();
    void updateFill();

}
