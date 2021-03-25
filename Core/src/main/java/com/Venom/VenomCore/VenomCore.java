package com.Venom.VenomCore;
import com.Venom.VenomCore.Commands.Fix.TimingsFix;
import com.Venom.VenomCore.Commands.Fix.TimingsFixCommand;
import com.Venom.VenomCore.External.Economy.EconomyManager;
import com.Venom.VenomCore.External.Jobs.JobsManager;
import com.Venom.VenomCore.External.Placeholder.PlaceholderManager;
import com.Venom.VenomCore.External.Protection.ProtectionManager;
import com.Venom.VenomCore.External.Skyblock.SkyBlockManager;
import com.Venom.VenomCore.Menu.Engine.MenuListener;
import com.Venom.VenomCore.Plugin.Settings.Metrics;
import com.Venom.VenomCore.Server.ServerVersion;
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