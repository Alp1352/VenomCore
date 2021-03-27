package com.venom.venomcore.nms.core.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public interface AnvilCore {

    AnvilContainer createAnvil(Player p);

    AnvilContainer createAnvil(Player p, InventoryHolder holder);

    AnvilContainer createAnvil(Player p, InventoryHolder holder, String title);
}
