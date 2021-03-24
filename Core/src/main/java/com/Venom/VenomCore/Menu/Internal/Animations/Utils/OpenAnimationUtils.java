package com.Venom.VenomCore.Menu.Internal.Animations.Utils;

import com.Venom.VenomCore.Compatibility.CompatibleMaterial;
import com.Venom.VenomCore.Item.ItemBuilder;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Util class for animation between GUIs.
 * ToDo : Rewrite vertical animations.
 * @author Alp Beji
 */
public class OpenAnimationUtils {
    public static List<HashMap<Integer, MenuItem>> fadeInVertical(HashMap<Integer, MenuItem> baseInventory, int size) {
        List<HashMap<Integer, MenuItem>> allInventories = new ArrayList<>();
        int loopedAmount = 0;
        while (loopedAmount < size / 9) {
            HashMap<Integer, MenuItem> newInventory = new HashMap<>();
            int count = 0;
            int start = loopedAmount * 9;
            while (count != 9) {
                newInventory.put(start, baseInventory.get(start));
                start = start + 1;
                count = count + 1;
            }
            allInventories.add(newInventory);
            loopedAmount = loopedAmount + 1;
        }
        return allInventories;
    }

    public static List<HashMap<Integer, MenuItem>> fadeOutVertical(HashMap<Integer, MenuItem> baseInventory, int size) {
        List<HashMap<Integer, MenuItem>> allInventories = new ArrayList<>();
        int loopedAmount = 0;
        while (loopedAmount < size / 9) {
            HashMap<Integer, MenuItem> newInventory;
            if (!allInventories.isEmpty()) {
                newInventory = new HashMap<>(allInventories.get(loopedAmount - 1));
            } else {
                newInventory = new HashMap<>(baseInventory);
            }
            int stop = size - (loopedAmount * 9);
            int count = 0;
            while (count != 9) {
                newInventory.put(stop - 1, new MenuItem(new ItemBuilder(CompatibleMaterial.AIR.parseMaterial())));
                count = count + 1;
                stop = stop - 1;
            }
            allInventories.add(newInventory);
            loopedAmount = loopedAmount + 1;
        }
        return allInventories;
    }

    public static List<Container> fadeInHorizontal(Container baseInventory, int size) {
        List<Container> allInventories = new ArrayList<>();
        Container newInventory;
        int loop = 0;
        boolean doubleUp = false;
        while (loop < 4) {
            if (!allInventories.isEmpty()) {
                Container oldInventory = allInventories.get(loop - 1);

                newInventory = new Container(size);
                oldInventory.copy(newInventory);

                emptyColumn(newInventory, 8, size);
                emptyColumn(newInventory, 0, size);
                newInventory.remove(4);

                // left
                for (int i = 0; i < 3; i++) {
                    fillColumn(newInventory, i + 1, getItemsInColumn(oldInventory, i, size), size);
                }

                // right
                for (int i = 8; i > 5; i--) {
                    fillColumn(newInventory, i - 1, getItemsInColumn(oldInventory, i, size), size);
                }

                // up
                for (int i = 0; i < (size / 9); i++) {
                    if (oldInventory.get(4 + (9 * i)) == null)
                        continue;

                    newInventory.set(oldInventory.get(4 + (9 * i)), 4 + (9 * (i + 1)));
                }
                if (doubleUp) {
                    Container copy = new Container(size);
                    newInventory.copy(copy);

                    copy.set(baseInventory.get((size - 5) - (9 * (loop))), 4);
                    for (int i = 0; i < (size / 9); i++) {
                        if (copy.get(4 + (9 * i)) == null)
                            continue;

                        newInventory.set(copy.get(4 + (9 * i)), 4 + (9 * (i + 1)));
                    }
                }
            } else {
                newInventory = new Container(size);
            }

            fillColumn(newInventory, 0, getItemsInColumn(baseInventory, 3 - loop, size), size);
            fillColumn(newInventory, 8, getItemsInColumn(baseInventory, 5 + loop, size), size);

            if (!doubleUp) {
                newInventory.set(baseInventory.get((size - 5) - (9 * loop)), 4);
            } else {
                newInventory.set(baseInventory.get((size - 5) - (9 * loop + 1)), 4);
            }
            doubleUp = !doubleUp;

            allInventories.add(newInventory);
            loop = loop + 1;
        }
        return allInventories;
    }

    public static List<Container> fadeOutHorizontal(Container baseInventory, int size) {
        List<Container> allInventories = new ArrayList<>();
        Container newInventory;
        int loop = 0;
        boolean doubleUp = false;
        while (loop < 4) {
            Container oldInventory;
            if (!allInventories.isEmpty()) {
                 oldInventory = allInventories.get(loop - 1);
            } else {
                oldInventory = new Container(size);
                baseInventory.copy(oldInventory);
            }

            newInventory = new Container(size);
            oldInventory.copy(newInventory);

            emptyColumn(newInventory, 5 + loop, size);
            emptyColumn(newInventory, 3 - loop, size);
            newInventory.remove((size - 5) - (9 * loop));

            // left
            for (int i = 3; i > 0; i--) {
                fillColumn(newInventory, i - 1, getItemsInColumn(oldInventory, i, size), size);
            }

            // right
            for (int i = 5; i < 8; i++) {
                fillColumn(newInventory, i + 1, getItemsInColumn(oldInventory, i, size), size);
            }

            // up ---
            for (int i = 0; i < (size / 9); i++) {
                if (oldInventory.get(4 + (9 * (i))) == null)
                    continue;

                newInventory.set(oldInventory.get(4 + (9 * (i + 1))), 4 + (9 * (i)));
            }

            if (doubleUp) {
                Container copy = new Container(size);
                newInventory.copy(copy);
                for (int i = 0; i < (size / 9); i++) {
                    if (copy.get(4 + (9 * (i))) == null)
                        continue;

                    newInventory.set(copy.get(4 + (9 * (i + 1))), 4 + (9 * (i)));
                }
                newInventory.remove((size - 5) - (9 * loop + 1));
            }

            doubleUp = !doubleUp;
            // ----

            allInventories.add(newInventory);
            loop = loop + 1;
        }
        return allInventories;
    }

    private static void emptyColumn(Container inventory, int column, int size) {
        for (int i = 0; i < (size / 9); i++) {
            inventory.remove(column + (9 * i));
        }
    }

    private static void fillColumn(Container inventory, int column, List<MenuItem> items, int size) {
        for (int i = 0; i < (size / 9); i++) {
            if (items.size() <= i || items.get(i) == null) {
                continue;
            }
            inventory.set(items.get(i), column + (9 * i));
        }
    }

    private static List<MenuItem> getItemsInColumn(Container inventory, int column, int size) {
        List<MenuItem> items = new ArrayList<>();
        for (int i = 0; i < (size / 9); i++) {
            if (inventory.get(column + (9 * i)) == null)
                continue;

            items.add(inventory.get(column + (9 * i)));
        }
        return items;
    }

}
