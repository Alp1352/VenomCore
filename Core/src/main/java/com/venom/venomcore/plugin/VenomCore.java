package com.venom.venomcore.plugin;
import com.venom.venomcore.plugin.commands.fix.TimingsFix;
import com.venom.venomcore.plugin.commands.fix.TimingsFixCommand;
import com.venom.venomcore.plugin.external.economy.EconomyManager;
import com.venom.venomcore.plugin.external.jobs.JobsManager;
import com.venom.venomcore.plugin.external.placeholder.PlaceholderManager;
import com.venom.venomcore.plugin.external.protection.ProtectionManager;
import com.venom.venomcore.plugin.external.skyblock.SkyBlockManager;
import com.venom.venomcore.plugin.menu.engine.MenuListener;
import com.venom.venomcore.plugin.plugin.settings.Metrics;
import com.venom.venomcore.plugin.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class VenomCore extends JavaPlugin {
    public static boolean debug;
    @Override
    public void onEnable() {
        EconomyManager.load(this);
        ProtectionManager.load(this);
        JobsManager.load(this);
        PlaceholderManager.load(this);
        SkyBlockManager.load(this);

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        new Metrics(this, 10314);

        saveDefaultConfig();
        debug = getConfig().getBoolean("debug");

        if (getConfig().getBoolean("fix_timings") && ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_8_R1) && ServerVersion.isServerVersionLowerThan(ServerVersion.v1_9_R1)) {
            TimingsFix timingsFix = new TimingsFix("VenomTimings");

            PluginCommand command = getCommand("venomtimings");
            if (command != null) {
                command.setExecutor(new TimingsFixCommand(timingsFix));
            }
        }
    }
}
