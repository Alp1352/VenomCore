package com.Venom.VenomCore.Menu.Internal.Animations.DefaultAnimations;

import com.Venom.VenomCore.Chat.Color;
import com.Venom.VenomCore.Compatibility.CompatibleMaterial;
import com.Venom.VenomCore.Item.ItemBuilder;
import com.Venom.VenomCore.Menu.Internal.Animations.Utils.OpenAnimationUtils;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;

import java.util.HashMap;
import java.util.List;

/**
 * @author Alp Beji
 * @apiNote A class which represents the horizontal
 * GUI switch animation.
 */
public class VenomAnimationHorizontal extends SwitchAnimation {
    private static final HashMap<Integer, Container> baseInventories = new HashMap<>();
    private static final HashMap<Integer, List<Container>> fadeInAnimation = new HashMap<>();
    private static final HashMap<Integer, List<Container>> fadeOutAnimation = new HashMap<>();

    public VenomAnimationHorizontal(int size) {
        super(size);
    }

    @Override
    public HashMap<Integer, Container> getInventories() {
        HashMap<Integer, Container> inventories = new HashMap<>();

        Container baseInventory = getBaseInventory(size);
        Container baseTargetInventory = getBaseInventory(targetSize);

        List<Container> fadeIn = fadeInAnimation.get(size);
        if (fadeIn == null) {
            fadeIn = OpenAnimationUtils.fadeInHorizontal(baseInventory, size);
            fadeInAnimation.put(size, fadeIn);
        }

        List<Container> fadeOut = fadeOutAnimation.get(targetSize);
        if (fadeOut == null) {
            fadeOut = OpenAnimationUtils.fadeOutHorizontal(baseTargetInventory, targetSize);
            fadeOutAnimation.put(targetSize, fadeOut);
        }

        int a = 1;
        for (Container inventory : fadeIn) {
            for (int i = 0; i < 2; i++) {
                inventories.put(a, inventory);
                a = a + 1;
            }
        }

        for (int i = 9; i < 16; i++) {
            inventories.put(i, baseInventory);
        }

        for (int i = 16; i< 25; i++) {
            inventories.put(i, baseTargetInventory);
        }

        // 24 - 8 = 16
        a = 25;
        for (Container inventory : fadeOut) {
            for (int i = 25; i < 27; i++) {
               inventories.put(a, inventory);
               a = a + 1;
            }
        }
        // 32 - 24 = 8
        return inventories;
    }


    @Override
    public int getTotalTicks() {
        return 32;
    }

    @Override
    public int getStayTicks() {
        return 16;
    }

    private Container getBaseInventory(int maxSize) {
        if (baseInventories.containsKey(maxSize)) {
            return baseInventories.get(maxSize);
        }

        Container baseInventory = new Container(maxSize);
        MenuItem green = new MenuItem(new ItemBuilder(CompatibleMaterial.GREEN_STAINED_GLASS_PANE.parseItem()).setName(Color.translate("&aVenom Workshop")));
        MenuItem black = new MenuItem(new ItemBuilder(CompatibleMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).setName(Color.translate("&8Venom Workshop")));

        baseInventory.fill(black);

        baseInventory.set(green, 11);
        baseInventory.set(green, 20);
        baseInventory.set(green, 30);
        baseInventory.set(green, 40);
        baseInventory.set(green, 32);
        baseInventory.set(green, 24);
        baseInventory.set(green, 15);

        baseInventories.put(maxSize, baseInventory);
        return baseInventory;
    }
}
