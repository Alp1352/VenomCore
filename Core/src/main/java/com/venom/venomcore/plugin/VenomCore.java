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
import com.venom.venomcore.plugin.server.ServerType;
import com.venom.venomcore.plugin.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VenomCore extends JavaPlugin {
    public static boolean DEBUG;
    public static boolean VENOM_TIMINGS;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        long before = System.currentTimeMillis();

        LOGGER = getLogger();

        // Inform the user about the version and the type of the server.
        // This also initializes both ServerVersion and ServerType enums.

        String NMS_VERSION = ServerVersion.getServerVersion().getNMSVersion();
        String READABLE_VERSION = ServerVersion.getServerVersion().getReadableVersion();
        String SERVER_TYPE = ServerType.getServerType().name();
        if (!ServerVersion.isSupported()) {
            LOGGER.log(Level.WARNING, NMS_VERSION + " surumu desteklenmemekte! VenomCore, kapatiliyor!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        LOGGER.log(Level.INFO, NMS_VERSION + "(" + READABLE_VERSION + ")" +" algilandi. Surumunuz destekleniyor.");
        LOGGER.log(Level.INFO, SERVER_TYPE + " sunucu turu algilandi.");

        // Now that we support this version of minecraft, we can start setting up VenomCore.

        // Start loading the hooks.

        EconomyManager.load();
        ProtectionManager.load();
        JobsManager.load();
        PlaceholderManager.load();
        SkyBlockManager.load();

        // Hooks are loaded succesfully.

        LOGGER.log(Level.INFO, "Eklenti baglama mekanizmasi basariyla yuklendi!");

        // Start initializing the NMS Manager.

        NMSManager.init();

        // NMSManager is initialized successfully.

        LOGGER.log(Level.INFO, "NMS yoneticisi basariyla kuruldu!");

        // Register the Event Listener for GUIs.

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        // Event Listener is registered successfully.

        // Start enabling metrics.

        new Metrics(this, 10314);

        // Metrics enabled.

        // Create the config file.

        saveDefaultConfig();

        // Config file is successfully created.
        // Now, we can access config to get values.


        FileConfiguration config = getConfig();

        // Whether to enable the debug mode or not.
        // If this is enabled, stack traces will be printed to the console.
        DEBUG = config.getBoolean("debug");

        // Whether to fix the timings or not.
        // Since timings in minecraft 1.8 is bugged,
        // We decided to basically fix that.
        VENOM_TIMINGS = config.getBoolean("fix_timings");

        if (VENOM_TIMINGS && ServerVersion.isOneEight()) {
            TimingsFix timingsFix = new TimingsFix("VenomTimings");

            PluginCommand command = getCommand("venomtimings");

            if (command == null) {
                Bukkit.getConsoleSender().sendMessage(Color.translate("&cVenom Timingste bir sorun oluştu!"));
                return;
            }

            command.setExecutor(new TimingsFixCommand(timingsFix));

            LOGGER.log(Level.INFO, "VenomTimings aktif edildi. /venomtimings komutu ile kullanabilirsiniz.");
        }

        long current = System.currentTimeMillis();

        LOGGER.log(Level.INFO, "VenomCore basariyla aktif edildi. (" + (current - before) + "ms)");
    }
}
