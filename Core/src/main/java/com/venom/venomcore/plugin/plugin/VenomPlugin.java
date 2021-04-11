package com.venom.venomcore.plugin.plugin;

import com.venom.venomcore.plugin.VenomCore;
import com.venom.venomcore.plugin.database.DatabaseType;
import com.venom.venomcore.plugin.external.dependency.Dependency;
import com.venom.venomcore.plugin.external.dependency.DependencyType;
import com.venom.venomcore.plugin.language.Locale;
import com.venom.venomcore.plugin.plugin.settings.Metrics;
import com.venom.venomcore.plugin.task.BukkitExecutor;
import de.leonhard.storage.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.logging.Level;

@SuppressWarnings("unused")
public abstract class VenomPlugin extends JavaPlugin {
    private final ConsoleCommandSender sender = Bukkit.getConsoleSender();
    private Yaml menuConfiguration;
    private boolean stop = false;

    private Locale locale;
    private DependencyManager dependencyManager;
    private PluginSettings settings;
    private Executor executor;

    @Override
    public void onEnable() {
        loadCustomDependencies();
        settings = new PluginSettings();
        dependencyManager = new DependencyManager();
        executor = new BukkitExecutor(this);
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GREEN + "=============================");
        sender.sendMessage(ChatColor.GRAY + getDescription().getName() + ChatColor.DARK_GREEN + " by VenomWorkshop <3!");
        sender.sendMessage(ChatColor.GRAY + "Versiyon: "+ ChatColor.YELLOW +   getDescription().getVersion());
        sender.sendMessage(ChatColor.GRAY + "Aksiyon: " + ChatColor.GREEN + "Aciliyor...");
        try {
            setupSettings();
            checkSettings();
            if (stop) {
                return;
            }
            setupConfig();
            DatabaseType data = loadDatabase();
            sender.sendMessage(ChatColor.GRAY + "Veritabani " + ChatColor.WHITE +  data.toString() + ChatColor.GRAY + " ile yuklendi!");
            String localeName = setupLocale();
            sender.sendMessage(ChatColor.GRAY + "Dil dosyasi " + ChatColor.WHITE + localeName + ChatColor.GRAY + " ile yuklendi!");
            onVenomPluginEnable();
        } catch (Exception x) {
            if (VenomCore.DEBUG) {
                x.printStackTrace();
                return;
            }
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + getDescription().getName() + " acilirken hata olustu!");
            sender.sendMessage(ChatColor.GRAY + "Discord sunucumuzdan destek alabilirsiniz.");
            sender.sendMessage(" ");
            disablePlugin();
        }
        sender.sendMessage(ChatColor.GREEN + "=============================");
        sender.sendMessage(" ");
        VenomCore.LOGGER.log(Level.INFO, getDescription().getName() + " adli eklentiye baglanildi.");
    }

    public void disablePlugin() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GREEN + "=============================");
        sender.sendMessage(ChatColor.GRAY + getDescription().getName() + ChatColor.DARK_GREEN + " by VenomWorkshop <3!");
        sender.sendMessage(ChatColor.GRAY + "Versiyon: "+ ChatColor.YELLOW +   getDescription().getVersion());
        sender.sendMessage(ChatColor.GRAY + "Aksiyon: " + ChatColor.RED + "Kapatiliyor...");
        try {
            onVenomPluginDisable();
        } catch (Throwable x) {
            if (VenomCore.DEBUG) {
                x.printStackTrace();
                return;
            }

            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + getDescription().getName() + " kapatilirken hata olustu!");
            sender.sendMessage(ChatColor.GRAY + "Discord sunucumuzdan destek alabilirsiniz.");
            sender.sendMessage(" ");
            disablePlugin();
        }
        sender.sendMessage(ChatColor.GREEN + "=============================");
        sender.sendMessage(" ");
    }

    private void checkSettings() {
        checkDependencies();
        if (settings.isMetrics()) {
            new Metrics(this, settings.getMetricsID());
        }
        /* TODO Add version checker.
         if (settings.isVersionChecker()) {

         }
        */
    }

    private void checkDependencies() {
        for (Dependency dependency : dependencyManager.getAllDependencies()) {
            DependencyType type = dependencyManager.getType(dependency);

            if (dependency.isEnabled()) {
                if (dependency.isSendingMessage()) {
                    sender.sendMessage(ChatColor.GRAY + dependency.getName() + ChatColor.GREEN + " eklentisine baglanildi!");
                }
            } else {
                if (dependency.isSendingMessage()) {
                    if (dependency.isMultiple()) {
                        sender.sendMessage(ChatColor.GRAY + dependency.getName() + ChatColor.RED + " eklentilerinden biri bulunamadi!");
                    } else {
                        sender.sendMessage(ChatColor.GRAY + dependency.getName() + ChatColor.RED + " eklentisi bulunamadi!");
                    }
                }
                if (type == DependencyType.HARD) {
                    stop = true;
                    disablePlugin();
                    return;
                } else {
                    if (dependency.isSendingMessage()) {
                        if (dependency.isMultiple()) {
                            sender.sendMessage(ChatColor.GRAY + "Bu eklentilerden birini kurarak eklentimizin tum ozelliklerinden faydalanabilirsiniz.");
                        } else {
                            sender.sendMessage(ChatColor.GRAY + "Bu eklentiyi kurarak eklentimizin tum ozelliklerinden faydalanabilirsiniz.");
                        }
                    }
                }
            }
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Yaml getMenuConfiguration() {
        return menuConfiguration;
    }

    public void setMenuConfiguration(Yaml yaml) {
        this.menuConfiguration = yaml;
    }

    public void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public PluginSettings getSettings() {
        return settings;
    }

    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }

    public Executor getBukkitExecutor() {
        return executor;
    }

    /**
     * Creates and sets the locale.
     * @return The locale name.
     */
    public abstract String setupLocale();

    /**
     * Loads the database.
     * @return The loaded database type.
     */
    public abstract DatabaseType loadDatabase();

    /**
     * Create and read the configuration file of this plugin.
     */
    public abstract void setupConfig();

    public abstract void onVenomPluginDisable();

    public abstract void onVenomPluginEnable();

    /**
     * Setup the settings of the plugin.
     * i.e. Disable Metrics.
     */
    public abstract void setupSettings();

    /**
     * Loads custom dependencies of a plugin.
     * This is for integration with our hook system.
     */
    public abstract void loadCustomDependencies();
}
