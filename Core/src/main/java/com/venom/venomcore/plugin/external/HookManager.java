package com.venom.venomcore.plugin.external;

import com.google.common.collect.Iterables;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class for managing hooks.
 * @param <T> The abstract class for hooks.
 */
@SuppressWarnings({"unused", "unchecked"})
public class HookManager<T extends Hook> {

    private final Map<PluginHook<?>, T> registered = new HashMap<>();
    private final Map<Plugin, T> preferred = new HashMap<>();
    private final Class<?> generic;

    private T current;

    public HookManager(Class<?> generic) {
        this.generic = generic;
    }

    /**
     * Loads all the hooks.
     */
    public void load() {
        registered.putAll(PluginHook.loadAll(generic)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (T) entry.getValue()))
        );

        current = Iterables.get(registered.values(), 0, null);
    }

    /**
     * Gets the current hook.
     * @return The current hook. If no enabled plugins were found,
     * returns null.
     */
    public T getCurrentHook(Plugin plugin) {
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
    public boolean setPreferredHook(Plugin plugin, String name) {
        return setPreferredHook(plugin, getHook(name));
    }

    /**
     * Sets the preferred hook.
     * @param plugin The plugin to set.
     * @return True if preferred hook is set.
     */
    public boolean setPreferredHook(Plugin plugin, PluginHook<?> hook) {
        return setPreferredHook(plugin, getHook(hook));
    }

    private boolean setPreferredHook(Plugin plugin, T hook) {
        preferred.put(plugin, hook);
        return hook != null && hook.isEnabled();
    }

    /**
     * Gets the hook.
     * @param name The name of the hook to get.
     * @return The hook.
     */
    public T getHook(String name) {
        return getHook(PluginHook.getHook(name));
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
        return Collections.unmodifiableCollection(registered.values());
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
    public boolean isLoaded() {
        return current != null;
    }

    /**
     * Gets the name of the current hook.
     * @return The name of the current hook.
     */
    public String getName() {
        return current != null ? current.getName() : null;
    }

}
