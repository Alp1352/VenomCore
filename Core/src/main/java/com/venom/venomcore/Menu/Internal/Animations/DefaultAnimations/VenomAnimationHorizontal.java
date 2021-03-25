package com.venom.venomcore.Menu.Internal.Animations.DefaultAnimations;

import com.venom.venomcore.Chat.Color;
import com.venom.venomcore.Compatibility.CompatibleMaterial;
import com.venom.venomcore.Item.ItemBuilder;
import com.venom.venomcore.Menu.Internal.Animations.Utils.OpenAnimationUtils;
import com.venom.venomcore.Menu.Internal.Containers.Container;
import com.venom.venomcore.Menu.Internal.Item.MenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Alp Beji
 * @apiNote A class which represents the horizontal
 * GUI switch animation.
 */
public class VenomAnimationHorizontal extends SwitchAnimation {
    private static final HashMap<Integer, Container> BASE_INVENTORIES = new HashMap<>();
    private static final HashMap<Integer, List<Container>> FADE_IN_ANIMATION = new HashMap<>();
    private static final HashMap<Integer, List<Container>> FADE_OUT_ANIMATION = new HashMap<>();

    public VenomAnimationHorizontal(int size) {
        super(size);
    }

    @Override
    public HashMap<Integer, Container> getInventories() {
        HashMap<Integer, Container> inventories = new HashMap<>();

        Container baseInventory = getBaseInventory(size);
        Container baseTargetInventory = getBaseInventory(targetSize);

        List<Container> fadeIn = FADE_IN_ANIMATION.get(size);
        if (fadeIn == null) {
            fadeIn = OpenAnimationUtils.fadeInHorizontal(baseInventory, size);
            FADE_IN_ANIMATION.put(size, fadeIn);
        }

        List<Container> fadeOut = FADE_OUT_ANIMATION.get(targetSize);
        if (fadeOut == null) {
            fadeOut = OpenAnimationUtils.fadeOutHorizontal(baseTargetInventory, targetSize);
            FADE_OUT_ANIMATION.put(targetSize, fadeOut);
        }

        int a = 1;
        for (Container inventory : fadeIn) {
            for (int i = 0; i < 2; i++) {
                inventories.put(a, inventory);
                a = a + 1;
            }
        }

        IntStream.range(9, 16)
                .forEach(tick -> inventories.put(tick, baseInventory));

        IntStream.range(16, 25)
                .forEach(tick -> inventories.put(tick, baseTargetInventory));

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
        if (BASE_INVENTORIES.containsKey(maxSize)) {
            return BASE_INVENTORIES.get(maxSize);
        }

        Container baseInventory = new Container(maxSize);

        MenuItem green = new MenuItem(
                new ItemBuilder(CompatibleMaterial.GREEN_STAINED_GLASS_PANE)
                        .setName(Color.translate("&aVenom Workshop")));

        MenuItem black = new MenuItem(
                new ItemBuilder(CompatibleMaterial.BLACK_STAINED_GLASS_PANE)
                        .setName(Color.translate("&8Venom Workshop")));

        baseInventory.fill(black);

        baseInventory.set(green, 11);
        baseInventory.set(green, 20);
        baseInventory.set(green, 30);
        baseInventory.set(green, 40);
        baseInventory.set(green, 32);
        baseInventory.set(green, 24);
        baseInventory.set(green, 15);

        BASE_INVENTORIES.put(maxSize, baseInventory);
        return baseInventory;
    }
}
