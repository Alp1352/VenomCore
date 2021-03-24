package com.venom.nms.v1_16_R2.Anvil;

import com.venom.nms.core.Anvil.AnvilContainer;
import com.venom.nms.v1_16_R2.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class AnvilCore implements com.venom.nms.core.Anvil.AnvilCore {
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
