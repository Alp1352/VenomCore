package com.venom.venomcore.nms.v1_8_R2.inventory;

import com.venom.venomcore.nms.core.inventory.TitleUpdaterCore;
import com.venom.venomcore.nms.v1_8_R2.NMSUtils;
import net.minecraft.server.v1_8_R2.ChatMessage;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.PacketPlayOutOpenWindow;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class TitleUpdater implements TitleUpdaterCore {
    @Override
    public boolean updateTitle(Player p, String newTitle) {
        Inventory inventory = p.getOpenInventory().getTopInventory();

        if (inventory.getType() != InventoryType.CHEST) return false;

        EntityPlayer entityPlayer = NMSUtils.toNMS(p);

        int windowID = entityPlayer.activeContainer.windowId;
        ChatMessage title = new ChatMessage(ChatColor.translateAlternateColorCodes('&', newTitle));
        int size = inventory.getSize();

        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowID, "minecraft:chest", title, size);
        NMSUtils.sendPacket(NMSUtils.toNMS(p), packet);
        return true;
    }
}
