package com.venom.venomcore.nms.v1_8_R2.anvil;

import com.venom.venomcore.nms.core.anvil.AnvilContainer;
import com.venom.venomcore.nms.core.anvil.AnvilSlot;
import com.venom.venomcore.nms.v1_8_R2.NMSUtils;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.craftbukkit.v1_8_R2.event.CraftEventFactory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
public class AnvilView extends ContainerAnvil implements AnvilContainer {
    private final EntityPlayer entityPlayer;
    private final Inventory upperInv;

    private IInventory BASIC_INV;

    private int cost = -1;

    private boolean lockDrop = true;

    private String renameText;
    private String title = "Repair";

    private static Field BASIC_INVENTORY;
    private static Field OWNER;

    static {
        try {
            BASIC_INVENTORY = ContainerAnvil.class.getDeclaredField("h");
            OWNER = InventorySubcontainer.class.getDeclaredField("bukkitOwner");

            BASIC_INVENTORY.setAccessible(true);
            OWNER.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public AnvilView(EntityPlayer player, InventoryHolder holder, String title) {
        super(player.inventory, player.world, new BlockPosition(0,0,0), player);
        this.entityPlayer = player;
        this.upperInv = getBukkitView().getTopInventory();

        try {
            BASIC_INV = (IInventory) BASIC_INVENTORY.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (holder != null) {
            try {
                OWNER.set(BASIC_INV, holder);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public void b(EntityHuman entityhuman) {
        if (lockDrop) {
            entityPlayer.inventory.setCarried(null);
            for (int i = 0; i < BASIC_INV.getSize(); i++) {
                this.BASIC_INV.splitWithoutUpdate(i);
            }
        }
        super.b(entityhuman);
    }

    @Override
    public void e() {
        super.e();
        if (cost > -1) {
            a = cost;
        }
    }

    @Override
    public void a(String newName) {
        super.a(newName);
        this.renameText = newName;
    }

    @Override
    public void open() {
        int c = entityPlayer.nextContainerCounter();

        NMSUtils.sendPacketSync(entityPlayer, new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage(title), 0));

        entityPlayer.activeContainer = this;

        entityPlayer.activeContainer.windowId = c;

        entityPlayer.activeContainer.addSlotListener(entityPlayer);
    }

    @Override
    public void update() {
        this.e();
    }

    @Override
    public void close() {
        CraftEventFactory.handleInventoryCloseEvent(entityPlayer);
        entityPlayer.activeContainer = entityPlayer.defaultContainer;
        NMSUtils.sendPacket(entityPlayer, new PacketPlayOutCloseWindow(entityPlayer.activeContainer.windowId));
    }

    @Override
    public void setItem(ItemStack item, AnvilSlot slot) {
        upperInv.setItem(slot.ordinal(), item);
    }

    @Override
    public Inventory toBukkit() {
        return upperInv;
    }

    @Override
    public String getCustomTitle() {
        return title;
    }

    @Override
    public void setCustomTitle(String title) {
        this.title = title;
    }

    @Override
    public String getRenameText() {
        return renameText;
    }

    @Override
    public void setRenameText(String text) {
        this.a(text);
    }

    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return cost > -1 ? cost : this.a;
    }

    @Override
    public boolean isDropLocked() {
        return lockDrop;
    }

    @Override
    public void setDropLocked(boolean drop) {
        this.lockDrop = drop;
    }
}