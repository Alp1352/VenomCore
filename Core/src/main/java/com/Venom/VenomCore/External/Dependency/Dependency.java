package com.Venom.VenomCore.External.Dependency;

import com.Venom.VenomCore.External.Economy.EconomyManager;
import com.Venom.VenomCore.External.Hook;
import com.Venom.VenomCore.External.HookManager;
import com.Venom.VenomCore.External.Jobs.JobsManager;
import com.Venom.VenomCore.External.Placeholder.PlaceholderManager;
import com.Venom.VenomCore.External.Protection.ProtectionManager;
import com.Venom.VenomCore.External.Skyblock.SkyBlockManager;
import com.Venom.VenomCore.VenomCore;
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
