package com.venom.venomcore.plugin.external;

import com.venom.venomcore.plugin.external.economy.types.ReserveHook;
import com.venom.venomcore.plugin.external.economy.types.VaultHook;
import com.venom.venomcore.plugin.external.jobs.types.JobsReborn.JobsRebornHook;
import com.venom.venomcore.plugin.external.placeholder.types.MVdWPlaceholderHook;
import com.venom.venomcore.plugin.external.placeholder.types.PlaceholderAPIHook;
import com.venom.venomcore.plugin.external.protection.types.GriefPreventionHook;
import com.venom.venomcore.plugin.external.protection.types.LandsHook;
import com.venom.venomcore.plugin.external.protection.types.RedProtectHook;
import com.venom.venomcore.plugin.external.protection.types.UltimateClaimsHook;
import com.venom.venomcore.plugin.external.skyblock.types.ASkyBlockHook;
import com.venom.venomcore.plugin.external.skyblock.types.FabledSkyBlockHook;
import com.venom.venomcore.plugin.external.skyblock.types.IridiumSkyBlockHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class representing plugin hooks.
 * @param <T> The abstract class i.e. EconomyHook.
 */
@SuppressWarnings("unused")
public class PluginHook<T extends Hook> {

    private static final Map<Class<? extends Hook>, PluginHook<?>> HOOKS = new HashMap<>();
    private static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

    static {
        // Economy
        new PluginHook<>(ReserveHook.class, "Reserve");
        new PluginHook<>(VaultHook.class, "Vault");
        // Protection
        new PluginHook<>(GriefPreventionHook.class, "GriefPrevention");
        new PluginHook<>(LandsHook.class, "Lands");
        new PluginHook<>(RedProtectHook.class, "RedProtect");
        new PluginHook<>(UltimateClaimsHook.class, "UltimateClaims");
        // Jobs
        new PluginHook<>(JobsRebornHook.class, "Jobs");
        // Placeholders
        new PluginHook<>(PlaceholderAPIHook.class, "PlaceholderAPI");
        new PluginHook<>(MVdWPlaceholderHook.class, "MVdWPlaceholderAPI");
        // SkyBlock
        new PluginHook<>(ASkyBlockHook.class, "ASkyBlock");
        new PluginHook<>(FabledSkyBlockHook.class, "FabledSkyBlock");
        new PluginHook<>(IridiumSkyBlockHook.class, "IridiumSkyBlock");
    }

    private final Class<? extends T> handler;
    private final String name;
    private PluginHook(Class<? extends T> handler, String name) {
        this.handler = handler;
        this.name = name;

        HOOKS.put(handler, this);
    }

    /**
     * Adds a plugin hook to the system.
     * @param hook The hook, i.e VaultHook
     * @param name The name, i.e Vault
     * @return The added plugin hook.
     */
    public static PluginHook<?> addHook(Class<? extends Hook> hook, String name) {
        return new PluginHook<>(hook, name);
    }

    /**
     * Get a plugin hook from name.
     * @param name The name of the plugin.
     * @return The Plugin Hook.
     */
    public static PluginHook<?> getHook(String name) {
        return HOOKS.values()
                .stream()
                .filter(pluginHook -> pluginHook.getPluginName().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }

    /**
     * Get a hook from the handler of the hook.
     * @param clazz The handling class.
     * @return The Plugin Hook.
     */
    public static PluginHook<?> getHook(Class<?> clazz) {
        return HOOKS.get(clazz);
    }

    /**
     * Loads all the hooks of a given type.
     * @param generic The type of the hooks that will be loaded. i.e EconomyHook.
     * @return The loaded hooks.
     */
    protected static Map<PluginHook<?>, Hook> loadAll(Class<?> generic) {
        Map<PluginHook<?>, Hook> loaded = new HashMap<>();

        getHooks(generic)
                .stream()
                .filter(PluginHook::isEnabled)
                .forEach(pluginHook -> loaded.put(pluginHook, pluginHook.load()));

        return loaded;
    }

    /**
     * Gets all the hooks of a type.
     * @param generic The super class. i.e EconomyHook.
     * @return All hooks of that type.
     */
    protected static List<PluginHook<?>> getHooks(Class<?> generic) {
        return HOOKS.entrySet()
                .stream()
                .filter(entry -> generic.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Loads this hook.
     * @return The loaded hook.
     */
    public Hook load() {
        try {
            return handler.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks if the hook is enabled or not.
     * @return True if the hook is enabled.
     */
    public boolean isEnabled() {
        return PLUGIN_MANAGER.isPluginEnabled(this.name);
    }

    /**
     * Get the plugin name.
     * @return The plugin name.
     */
    public String getPluginName() {
        return name;
    }
}
