package com.venom.venomcore.nms.v1_13_R1.anvil;

import com.venom.venomcore.nms.core.anvil.AnvilContainer;
import com.venom.venomcore.nms.v1_13_R1.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class AnvilCore implements com.venom.venomcore.nms.core.anvil.AnvilCore {
    @Override
    public AnvilContainer createAnvil(Player p) {
        return createAnvil(p, null);
    }

    @Override
    public AnvilContainer createAnvil(Player p, InventoryHolder holder) {
        return createAnvil(p, holder, null);
    }

    @Override
    public AnvilContainer createAnvil(Player p, InventoryHolder holder, String title) {
        return new AnvilView(NMSUtils.toNMS(p), holder, title);
    }
}
