package com.Venom.VenomCore.Menu.Internal.Utils;

import com.Venom.VenomCore.Chat.Color;
import com.Venom.VenomCore.Compatibility.CompatibleMaterial;
import com.Venom.VenomCore.Compatibility.CompatibleSound;
import com.Venom.VenomCore.Item.ItemBuilder;
import com.Venom.VenomCore.Menu.GUI;
import com.Venom.VenomCore.Menu.Internal.Animations.Frame;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Item.Action.ClickAction;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import de.leonhard.storage.sections.FlatFileSection;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class MenuUtils {
    private final VenomPlugin plugin;
    private final String menuName;
    private final GUI gui;
    public MenuUtils(VenomPlugin plugin, String menuName, GUI gui) {
        this.plugin = plugin;
        this.menuName = menuName;
        this.gui = gui;
    }

    // Decorations start
    public List<ConfigItem> getDecorations() {
        List<ConfigItem> items = new ArrayList<>();
        FlatFileSection section = plugin.getMenuConfiguration().getSection(menuName + ".items.decorations");
        for (String key : section.singleLayerKeySet()) {
            MenuItem item = new MenuItem(getItem("decorations." + key));
            Object slotObj = section.get(key + ".slot");

            // Check if multiple slots are entered.
            List<Integer> slots = new ArrayList<>();
            if (slotObj instanceof String) {
                slots.add(Integer.parseInt((String) slotObj));
            } else if (slotObj instanceof List) {
                ((List<?>) slotObj).forEach(slot -> slots.add(Integer.parseInt((String) slot)));
            }

            // Convert the list to array.
            Integer[] array = new Integer[slots.size()];
            array = slots.toArray(array);
            items.add(new ConfigItem(item, array));
        }

        return items;
    }

    public void setDecorations() {
        List<ConfigItem> decorations = getDecorations();
        decorations.forEach(decoration -> {
            for (Integer slot : decoration.getSlots()) {
                gui.getUpperContainer().set(decoration.getMenuItem(), slot);
            }
        });
    }


    // Decorations end

    // Setup Item Start

    public void setupItem(String itemName, ClickAction action) {
        setupItem(itemName, new HashMap<>(), action);
    }

    public void setupItem(String itemName, HashMap<String, String> placeholders, ClickAction action) {
        setupItem(gui.getUpperContainer(), itemName, placeholders, action);
    }

    public void setupItem(Container container, String itemName, ClickAction action) {
        setupItem(container, itemName, new HashMap<>(), action);
    }

    public void setupItem(Container container, String itemName, HashMap<String, String> placeholders, ClickAction action) {
        setupItem(container, itemName, placeholders, action, getSlotOf(itemName));
    }

    public void setupItem(Container container, String itemName, HashMap<String, String> placeholders, ClickAction action, int slot) {
        MenuItem menuItem = new MenuItem(getItem(itemName, placeholders));
        menuItem.addAction(action, ClickType.LEFT, ClickType.RIGHT);
        menuItem.setSound(CompatibleSound.BLOCK_NOTE_BLOCK_HAT.parseSound());
        if (slot >= container.getSize()) {
            slot = slot - container.getSize();
        }
        container.set(menuItem, slot);
    }

    // Setup Item End

    // Item slot start

    public int getSlotOf(String itemName) {
        return plugin.getMenuConfiguration().getInt(menuName + ".items." + itemName + ".slot");
    }

    // Item slot end

    // Frame Start

    public Frame getFrame(String animationName) {
        return getFrame(gui.getUpperContainer(), animationName);
    }

    public Frame getFrame(Container container, String animationName) {
        FlatFileSection section = plugin.getMenuConfiguration().getSection(menuName);
        Frame frame = new Frame(container, section.getInt("animations.slot"));

        FlatFileSection loopSection = plugin.getMenuConfiguration().getSection(menuName + ".animations." + animationName);
        for (String s : loopSection.singleLayerKeySet()) {
            ItemBuilder builder;
            if (!section.getString("animations." + animationName + "." + s + ".material").contains("basehead")) {
                builder = new ItemBuilder(CompatibleMaterial.matchCompatibleMaterial(section.getString("animations." + animationName + "." + s + ".material") + ":" + section.getInt("animations." + animationName + "." + s + ".data")).orElse(CompatibleMaterial.BEDROCK).parseItem());
                builder.setDurability((short) section.getInt("animations." + animationName + "." + s + ".data"));
            } else {
                builder = new ItemBuilder(section.getString("animations." + animationName + "." + s + ".material").substring(9));
            }
            builder.setName(Color.translate(section.getString("animations." + animationName + "." + s + ".name")));
            builder.setLore(section.getStringList("animations." + animationName + "." + s + ".lore"));
            builder.setAmount(section.getInt("animations." + animationName + "." + s + ".amount"));
            MenuItem item = new MenuItem(builder);
            frame.addItem(item);
        }
        return frame;
    }

    // Frame End
    // Get Item Start
    public ItemBuilder getItem(String itemName) {
        return getItem(plugin, menuName, itemName);
    }

    public ItemBuilder getItem(String itemName, HashMap<String, String> placeholders) {
        return getItem(plugin, menuName, itemName, placeholders);
    }

    public static ItemBuilder getItem(VenomPlugin plugin, String menuName, String itemName) {
        return getItem(plugin, menuName, itemName, new HashMap<>());
    }

    public static ItemBuilder getItem(VenomPlugin plugin, String menuName, String itemName, HashMap<String, String> placeholders) {
        FlatFileSection section = plugin.getMenuConfiguration().getSection(menuName);
        ItemBuilder builder;
        if (!section.getString("items." + itemName + ".material").contains("basehead")) {
            builder = new ItemBuilder(CompatibleMaterial.matchCompatibleMaterial(section.getString("items." + itemName + ".material") + ":" + section.getInt("items." + itemName + ".data")).orElse(CompatibleMaterial.BEDROCK).parseItem());
            builder.setDurability((short) section.getInt("items." + itemName + ".data"));
        } else {
            builder = new ItemBuilder(section.getString("items." + itemName + ".material").substring(9));
        }
        builder.setName(Color.translate(section.getString("items." + itemName + ".name")));
        builder.setLore(section.getStringList("items." + itemName + ".lore"));
        builder.setAmount(section.getInt("items." + itemName + ".amount"));
        return setPlaceholder(builder, placeholders);
    }

    // Get Item End

    private static String setPlaceholder(String str, Map<String, String> map) {
        if (map == null)
            return str;
        for (Map.Entry<String, String> entr : map.entrySet()) {
            str = str.replace(entr.getKey(), entr.getValue());
        }
        return str;
    }

    private static ItemBuilder setPlaceholder(ItemBuilder item, Map<String, String> map) {
        if (item.getLore() != null) {
            List<String> list = item.getLore().stream().map(x -> setPlaceholder(x, map)).collect(Collectors.toList());
            item.setLore(list);
        }
        if (item.getName() != null) {
            item.setName(setPlaceholder(item.getName(), map));
        }
        return item;
    }

    public static HashMap<String, String> withPlaceholders(String replaced, String replace) {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put(replaced, replace);
        return placeholders;
    }

    public static String getTitle(VenomPlugin plugin, String menuName) {
        return Color.translate(plugin.getMenuConfiguration().getString(menuName + ".title"));
    }

    public static int getSize(VenomPlugin plugin, String menuName) {
        return plugin.getMenuConfiguration().getInt(menuName + ".slot");
    }

    public static class ConfigItem {
        private final MenuItem item;
        private final Integer[] slots;
        private ConfigItem(MenuItem item, Integer... slot) {
            this.item = item;
            this.slots = slot;
        }

        public Integer[] getSlots() {
            return slots;
        }

        public MenuItem getMenuItem() {
            return item;
        }
    }
}
