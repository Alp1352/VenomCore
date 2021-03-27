package com.venom.venomcore.nms.v1_14_R1.anvil;


import com.venom.venomcore.nms.core.anvil.AnvilContainer;
import com.venom.venomcore.nms.core.anvil.AnvilSlot;
import com.venom.venomcore.nms.v1_14_R1.NMSUtils;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.event.CraftEventFactory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    private static Method PROPERTY_SET;
    private static Method PROPERTY_GET;

    private static final boolean ONE_FOURTEEN = Bukkit.getBukkitVersion().contains("1.14.4");

    static {
        try {
            BASIC_INVENTORY = ContainerAnvil.class.getDeclaredField("repairInventory");
            OWNER = InventorySubcontainer.class.getDeclaredField("bukkitOwner");
            TITLE = Container.class.getDeclaredField("title");

            BASIC_INVENTORY.setAccessible(true);
            OWNER.setAccessible(true);
            TITLE.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    static {
        if (!ONE_FOURTEEN) {
            try {
                PROPERTY_SET = ContainerProperty.class.getDeclaredMethod("a", int.class);
                PROPERTY_GET = ContainerProperty.class.getDeclaredMethod("b");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public AnvilView(EntityPlayer player, InventoryHolder holder, String title) {
        super(player.nextContainerCounter(), player.inventory, ContainerAccess.at(player.world, new BlockPosition(0,0,0)));
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
            entityPlayer.inventory.setCarried(net.minecraft.server.v1_14_R1.ItemStack.a);
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
            ContainerProperty property = this.levelCost;
            if (ONE_FOURTEEN) {
                property.set(cost);
            } else {
                try {
                    PROPERTY_SET.invoke(property, cost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
        ContainerProperty property = this.levelCost;
        if (cost > -1) {
            return cost;
        }

        if (ONE_FOURTEEN) {
            return property.get();
        } else {
            try {
                return (int) PROPERTY_GET.invoke(property);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return cost;
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
