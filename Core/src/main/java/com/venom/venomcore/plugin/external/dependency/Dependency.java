package com.venom.venomcore.plugin.external.dependency;

import com.venom.venomcore.plugin.VenomCore;
import com.venom.venomcore.plugin.external.Hook;
import com.venom.venomcore.plugin.external.HookManager;
import com.venom.venomcore.plugin.external.economy.EconomyManager;
import com.venom.venomcore.plugin.external.jobs.JobsManager;
import com.venom.venomcore.plugin.external.placeholder.PlaceholderManager;
import com.venom.venomcore.plugin.external.protection.ProtectionManager;
import com.venom.venomcore.plugin.external.skyblock.SkyBlockManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Dependency {
    public static final Dependency ECONOMY = new Dependency(VenomCore.getProvidingPlugin(VenomCore.class), EconomyManager.getManager());
    public static final Dependency PROTECTION = new Dependency(VenomCore.getProvidingPlugin(VenomCore.class), ProtectionManager.getManager());
    public static final Dependency JOBS = new Dependency(VenomCore.getProvidingPlugin(VenomCore.class), JobsManager.getManager());
    public static final Dependency PLACEHOLDER = new Dependency(VenomCore.getProvidingPlugin(VenomCore.class), PlaceholderManager.getManager());
    public static final Dependency SKYBLOCK = new Dependency(VenomCore.getProvidingPlugin(VenomCore.class), SkyBlockManager.getManager());

    private final boolean enabled;
    private final String name;
    private final boolean multiple;
    private boolean message = true;

    public Dependency(JavaPlugin plugin, HookManager<?> manager) {
        Hook hook = manager.getCurrentHook(plugin);

        enabled = hook != null && hook.isEnabled();

        if (hook != null) {
            name = hook.getName();
            multiple = false;
            return;
        }

        StringBuilder builder = new StringBuilder();

        List<String> possible = manager.getPossiblePlugins();
        possible.forEach(name -> builder.append(name).append(", "));

        multiple = possible.size() > 1;

        name = builder.substring(0, builder.length() - 2);
    }

    public boolean isMultiple() {
        return multiple;
    }

    public boolean isSendingMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
