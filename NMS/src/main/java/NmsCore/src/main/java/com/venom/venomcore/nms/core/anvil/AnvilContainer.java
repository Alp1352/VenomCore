package com.venom.venomcore.nms.core.anvil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface AnvilContainer {
    void open();

    void close();

    void update();

    String getRenameText();

    void setRenameText(String text);

    int getCost();

    void setCost(int cost);

    String getCustomTitle();

    void setCustomTitle(String title);

    void setItem(ItemStack item, AnvilSlot slot);

    boolean isDropLocked();

    void setDropLocked(boolean drop);

    Inventory toBukkit();
}
