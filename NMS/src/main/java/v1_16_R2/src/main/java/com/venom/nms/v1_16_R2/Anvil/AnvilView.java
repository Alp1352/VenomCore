package com.venom.nms.v1_16_R2.Anvil;


import com.venom.nms.core.Anvil.AnvilContainer;
import com.venom.nms.core.Anvil.AnvilSlot;
import com.venom.nms.v1_16_R2.NMSUtils;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.craftbukkit.v1_16_R2.event.CraftEventFactory;
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

    private String title = "Repair";

    private static Field BASIC_INVENTORY;
    private static Field OWNER;
    private static Field TITLE;

    static {
        try {
            BASIC_INVENTORY = ContainerAnvilAbstract.class.getDeclaredField("repairInventory");
            OWNER = InventorySubcontainer.class.getDeclaredField("bukkitOwner");
            TITLE = Container.class.getDeclaredField("title");

            TITLE.setAccessible(true);
            BASIC_INVENTORY.setAccessible(true);
            OWNER.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public AnvilView(EntityPlayer player, InventoryHolder holder, String title) {
        super(player.nextContainerCounter(), player.inventory,ContainerAccess.at(player.world, new BlockPosition(0, 0, 0)));
        checkReachable = false;
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
            setTitle(new ChatMessage(title));
        }
    }

    @Override
    public void b(EntityHuman entityhuman) {
        if (lockDrop) {
            entityPlayer.inventory.setCarried(net.minecraft.server.v1_16_R2.ItemStack.b);
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
            levelCost.set(cost);
        }
    }

    @Override
    public void open() {

        NMSUtils.sendPacketSync(entityPlayer, new PacketPlayOutOpenWindow(windowId, Containers.ANVIL, new ChatMessage(title)));

        entityPlayer.activeContainer = this;

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
        try {
            setTitle(new ChatMessage(title));
        } catch (IllegalStateException e) {
            try {
                TITLE.set(this, title);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getRenameText() {
        return super.renameText;
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
        return cost > -1 ? cost : this.levelCost.get();
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
