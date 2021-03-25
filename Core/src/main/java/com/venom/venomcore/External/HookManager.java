package com.venom.venomcore.External;

import com.venom.venomcore.Plugin.VenomPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class for managing hooks.
 * @param <T> The abstract class for hooks.
 */
public class HookManager<T extends Hook> {

    private final Class<?> generic;
    private T current;

    private Collection<T> VALUES;

    private final Map<PluginHook<?>, T> registered = new HashMap<>();
    private final Map<JavaPlugin, T> preferred = new HashMap<>();
    private final Set<JavaPlugin> loaded = new HashSet<>();

    public HookManager(Class<?> generic) {
        this.generic = generic;
    }
    /**
     * Loads all the hooks.
     * @param plugin The plugin loading.
     */
    public void load(JavaPlugin plugin) {
        if (loaded.contains(plugin))
            return;

        registered.putAll(PluginHook.loadAll(generic, plugin)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (T) e.getValue())));

        VALUES = registered.values();

        if (!VALUES.isEmpty()) {
            current = VALUES.iterator().next();
        }

        loaded.add(plugin);
    }

    /**
     * Gets the current hook.
     * @return The current hook. If no enabled plugins were found,
     * returns null.
     */
    public T getCurrentHook(JavaPlugin plugin) {
        return preferred.getOrDefault(plugin, current);
    }

    /**
     * Sets the preferred hook. For example,
     * If somehow both vault and reserve is enabled,
     * Use this to choose one of them.
     * @param name The name of the plugin. i.e
     *             If I want to use Vault, Vault.
     * @return True if preferred hook is set.
     */
    public boolean setPreferredHook(VenomPlugin plugin, String name) {
        return setPreferredHook(plugin, getHook(name));
    }

    /**
     * Sets the preferred hook.
     * @param plugin The plugin to set.
     * @return True if preferred hook is set.
     */
    public boolean setPreferredHook(VenomPlugin venomPlugin, PluginHook<?> plugin) {
        return setPreferredHook(venomPlugin, getHook(plugin));
    }

    private boolean setPreferredHook(VenomPlugin plugin, T hook) {
        if (hook != null) {
            preferred.put(plugin, hook);
            return true;
        }

        return false;
    }

    /**
     * Gets the hook.
     * @param name The name of the hook to get.
     * @return The hook.
     */
    public T getHook(String name) {
        for (PluginHook<?> pluginHook : registered.keySet()) {
            if (pluginHook.getPluginName().equalsIgnoreCase(name)) {
                return getHook(pluginHook);
            }
        }

        return null;
    }

    /**
     * Gets the hook.
     * @param pluginHook The hook to get.
     * @return The hook.
     */
    public T getHook(PluginHook<?> pluginHook) {
        return registered.get(pluginHook);
    }

    /**
     * Get all registered hooks in the system.
     * @return All enabled hooks.
     */
    public Collection<T> getRegisteredHooks() {
        return Collections.unmodifiableCollection(VALUES);
    }

    /**
     * Get all registered plugins in the system.
     * @return All registered plugins.
     */
    public List<String> getRegisteredPlugins() {
        return registered.keySet()
                .stream()
                .map(PluginHook::getPluginName)
                .collect(Collectors.toList());
    }

    /**
     * Get all possible plugins. Contains
     * plugins that are NOT enabled.
     * @return All the possible plugins.
     */
    public List<String> getPossiblePlugins() {
        return PluginHook.getHooks(generic)
                .stream()
                .map(PluginHook::getPluginName)
                .collect(Collectors.toList());
    }

    /**
     * Check if a hook is enabled.
     * @param name The hook name.
     * @return True if the hook is enabled.
     */
    public boolean isEnabled(String name) {
        return getHook(name) != null;
    }

    /**
     * Check if a hook is enabled.
     * @param pluginHook The hook name.
     * @return True if the hook is enabled.
     */
    public boolean isEnabled(PluginHook<?> pluginHook) {
        return registered.containsKey(pluginHook);
    }

    /**
     * Check if this manager is enabled.
     * @return True if the manager is enabled.
     */
    public boolean isEnabled() {
        return current != null;
    }

    /**
     * Check if this manager is loaded
     * or not.
     * @return True if the manager is loaded.
     */
    public boolean isLoaded(JavaPlugin plugin) {
        return loaded.contains(plugin);
    }

    /**
     * Gets the name of the current hook.
     * @return The name of the current hook.
     */
    public String getName() {
        return current != null ? current.getName() : null;
    }

}
