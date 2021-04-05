package com.venom.venomcore.plugin;
import com.venom.venomcore.plugin.chat.Color;
import com.venom.venomcore.plugin.commands.fix.TimingsFix;
import com.venom.venomcore.plugin.commands.fix.TimingsFixCommand;
import com.venom.venomcore.plugin.external.economy.EconomyManager;
import com.venom.venomcore.plugin.external.jobs.JobsManager;
import com.venom.venomcore.plugin.external.placeholder.PlaceholderManager;
import com.venom.venomcore.plugin.external.protection.ProtectionManager;
import com.venom.venomcore.plugin.external.skyblock.SkyBlockManager;
import com.venom.venomcore.plugin.menu.engine.MenuListener;
import com.venom.venomcore.plugin.nms.NMSManager;
import com.venom.venomcore.plugin.plugin.settings.Metrics;
import com.venom.venomcore.plugin.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class VenomCore extends JavaPlugin {
    public static boolean DEBUG;
    public static boolean VENOM_TIMINGS;

    @Override
    public void onEnable() {
        EconomyManager.load();
        ProtectionManager.load();
        JobsManager.load();
        PlaceholderManager.load();
        SkyBlockManager.load();
        // Hooks loaded.

        NMSManager.init();

        // NMSManager is initialized. That was a heavy task lol.

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        new Metrics(this, 10314);

        saveDefaultConfig();

        FileConfiguration config = getConfig();
        DEBUG = config.getBoolean("debug");
        VENOM_TIMINGS = config.getBoolean("fix_timings");

        if (VENOM_TIMINGS && ServerVersion.isOneEight()) {
            TimingsFix timingsFix = new TimingsFix("VenomTimings");

            PluginCommand command = getCommand("venomtimings");

            if (command == null) {
                Bukkit.getConsoleSender().sendMessage(Color.translate("&cVenom Timingste bir sorun oluştu!"));
                return;
            }

            command.setExecutor(new TimingsFixCommand(timingsFix));
        }
    }
}
