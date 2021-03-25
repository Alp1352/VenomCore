package com.venom.venomcore;
import com.venom.venomcore.Commands.Fix.TimingsFix;
import com.venom.venomcore.Commands.Fix.TimingsFixCommand;
import com.venom.venomcore.External.Economy.EconomyManager;
import com.venom.venomcore.External.Jobs.JobsManager;
import com.venom.venomcore.External.Placeholder.PlaceholderManager;
import com.venom.venomcore.External.Protection.ProtectionManager;
import com.venom.venomcore.External.Skyblock.SkyBlockManager;
import com.venom.venomcore.Menu.Engine.MenuListener;
import com.venom.venomcore.Plugin.Settings.Metrics;
import com.venom.venomcore.Server.ServerVersion;
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
