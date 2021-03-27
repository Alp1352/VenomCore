package com.venom.venomcore.plugin.external;

import com.venom.venomcore.plugin.external.economy.EconomyHook;
import com.venom.venomcore.plugin.external.economy.types.ReserveHook;
import com.venom.venomcore.plugin.external.economy.types.VaultHook;
import com.venom.venomcore.plugin.external.jobs.JobsHook;
import com.venom.venomcore.plugin.external.jobs.types.JobsReborn.JobsRebornHook;
import com.venom.venomcore.plugin.external.placeholder.PlaceholderHook;
import com.venom.venomcore.plugin.external.placeholder.types.MVdWPlaceholderHook;
import com.venom.venomcore.plugin.external.placeholder.types.PlaceholderAPIHook;
import com.venom.venomcore.plugin.external.protection.ProtectionHook;
import com.venom.venomcore.plugin.external.protection.types.GriefPreventionHook;
import com.venom.venomcore.plugin.external.protection.types.LandsHook;
import com.venom.venomcore.plugin.external.protection.types.RedProtectHook;
import com.venom.venomcore.plugin.external.protection.types.UltimateClaimsHook;
import com.venom.venomcore.plugin.external.skyblock.SkyBlockHook;
import com.venom.venomcore.plugin.external.skyblock.types.ASkyBlockHook;
import com.venom.venomcore.plugin.external.skyblock.types.FabledSkyBlockHook;
import com.venom.venomcore.plugin.external.skyblock.types.IridiumSkyBlockHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class representing plugin hooks.
 * @param <T> The abstract class i.e. EconomyHook.
 */
public class PluginHook<T extends Class<?>> {

    protected static final Map<Class<?>, PluginHook<?>> hooks;

    static {
        hooks = new HashMap<>();
        // Economy
        new PluginHook(EconomyHook.class, ReserveHook.class, "Reserve");
        new PluginHook(EconomyHook.class, VaultHook.class, "Vault");
        // Protection
        new PluginHook(ProtectionHook.class, GriefPreventionHook.class, "GriefPrevention");
        new PluginHook(ProtectionHook.class, LandsHook.class, "Lands");
        new PluginHook(ProtectionHook.class, RedProtectHook.class, "RedProtect");
        new PluginHook(ProtectionHook.class, UltimateClaimsHook.class, "UltimateClaims");
        // Jobs
        new PluginHook(JobsHook.class, JobsRebornHook.class, "Jobs");
        // Placeholders
        new PluginHook(PlaceholderHook.class, PlaceholderAPIHook.class, "PlaceholderAPI");
        new PluginHook(PlaceholderHook.class, MVdWPlaceholderHook.class, "MVdWPlaceholderAPI");
        // SkyBlock
        new PluginHook(SkyBlockHook.class, ASkyBlockHook.class, "ASkyBlock");
        new PluginHook(SkyBlockHook.class, FabledSkyBlockHook.class, "FabledSkyBlock");
        new PluginHook(SkyBlockHook.class, IridiumSkyBlockHook.class, "IridiumSkyBlock");
    }

    private final T type;
    private final Class<? extends T> handler;
    private final String name;
    private Constructor<?> pluginConstructor;
    private PluginHook(T type, Class<? extends T> handler, String name) {
        this.type = type;
        this.handler = handler;
        this.name = name;
        hooks.put(handler, this);
        try {
            pluginConstructor = handler.getDeclaredConstructor(JavaPlugin.class);
        } catch (NoSuchMethodException | SecurityException ex) { pluginConstructor = null; }
    }

    /**
     * Adds a plugin hook to the system.
     * @param type The type, i.e EconomyHook
     * @param hook The hook, i.e VaultHook
     * @param name The name, i.e Vault
     * @return The added plugin hook.
     */
    public static PluginHook<?> addHook(Class<?> type, Class<?> hook, String name) {
        return new PluginHook(type, hook, name);
    }

    /**
     * Loads all the hooks of a given type.
     * @param generic The type of the hooks that will be loaded. i.e EconomyHook.
     * @param plugin The plugin.
     * @return The loaded hooks.
     */
    protected static Map<PluginHook<?>, Hook> loadAll(Class<?> generic, JavaPlugin plugin) {
        Map<PluginHook<?>, Hook> loaded = new HashMap<>();

        PluginManager manager = Bukkit.getPluginManager();

        for (PluginHook<?> pluginHook : getHooks(generic)) {
            if (manager.isPluginEnabled(pluginHook.getPluginName())) {
                loaded.put(pluginHook, (Hook) pluginHook.load(plugin));
            }
        }
        return loaded;
    }

    /**
     * Gets all the hooks of a type.
     * @param type The type. i.e EconomyHook.
     * @return All hooks of that type.
     */
    protected static List<PluginHook<?>> getHooks(Class<?> type) {
        return hooks.entrySet()
                .stream()
                .filter(entry -> entry.getKey() == type || entry.getValue().getHandler() == type || type.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Loads this hook.
     * @param plugin The plugin loading.
     * @return The instance of the hook.
     */
    public Object load(JavaPlugin plugin) {
        try {
            return pluginConstructor != null ? pluginConstructor.newInstance(plugin) : handler.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Loads this hook.
     * @return The loaded hook.
     */
    public Object load() {
        return load(null);
    }

    /**
     * Checks if the hook is enabled or not.
     * @return True if the hook is enabled.
     */
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(this.name);
    }

    protected T getType() {
        return type;
    }

    protected Class<? extends T> getHandler() {
        return handler;
    }

    public String getPluginName() {
        return name;
    }
}
