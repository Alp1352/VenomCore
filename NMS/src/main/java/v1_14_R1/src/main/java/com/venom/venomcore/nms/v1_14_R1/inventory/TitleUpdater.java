package com.venom.venomcore.nms.v1_14_R1.inventory;

import com.venom.venomcore.nms.core.inventory.TitleUpdaterCore;
import com.venom.venomcore.nms.v1_14_R1.NMSUtils;
import net.minecraft.server.v1_14_R1.*;
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

        Container container = Container.getFromSize(inventory.getSize());

        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowID, container.toNMS(), title);
        NMSUtils.sendPacket(NMSUtils.toNMS(p), packet);
        return true;
    }

    private enum Container {
        CHEST_9X1(Containers.GENERIC_9X1),
        CHEST_9X2(Containers.GENERIC_9X2),
        CHEST_9X3(Containers.GENERIC_9X3),
        CHEST_9X4(Containers.GENERIC_9X4),
        CHEST_9X5(Containers.GENERIC_9X5),
        CHEST_9X6(Containers.GENERIC_9X6);

        private final Containers<ContainerChest> chestContainer;

        Container(Containers<ContainerChest> chestContainer) {
            this.chestContainer = chestContainer;
        }

        public Containers<ContainerChest> toNMS() {
            return chestContainer;
        }

        public static Container getFromSize(int size) {
            return valueOf("CHEST_9X" + size / 9);
        }
    }
}
