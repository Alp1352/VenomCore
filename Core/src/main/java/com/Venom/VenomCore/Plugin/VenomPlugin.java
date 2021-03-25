package com.Venom.VenomCore.Plugin;

import com.Venom.VenomCore.Database.DatabaseType;
import com.Venom.VenomCore.External.Dependency.Dependency;
import com.Venom.VenomCore.External.Dependency.DependencyType;
import com.Venom.VenomCore.Language.Locale;
import com.Venom.VenomCore.Plugin.Settings.Metrics;
import com.Venom.VenomCore.Task.BukkitExecutor;
import com.Venom.VenomCore.VenomCore;
import de.leonhard.storage.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;

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
        loadDependencies();
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
            sender.sendMessage(ChatColor.GREEN + "=============================");
            sender.sendMessage(" ");
        } catch (Throwable x) {
            if (VenomCore.debug) {
                x.printStackTrace();
                return;
            }
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + getDescription().getName() + " acilirken hata olustu!");
            sender.sendMessage(ChatColor.GRAY + "Discord sunucumuzdan destek alabilirsiniz.");
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GREEN + "=============================");
            sender.sendMessage(" ");
            disablePlugin();
        }
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
            sender.sendMessage(ChatColor.GREEN + "=============================");
            sender.sendMessage(" ");
        } catch (Throwable x) {
            if (VenomCore.debug) {
                x.printStackTrace();
                return;
            }

            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + getDescription().getName() + " kapatilirken hata olustu!");
            sender.sendMessage(ChatColor.GRAY + "Discord sunucumuzdan destek alabilirsiniz.");
            sender.sendMessage(" ");
            disablePlugin();
        }
    }

    private void loadDependencies() {
        loadCustomDependencies();
    }

    private void checkSettings() {
        checkDependencies();
        if (settings.isMetrics()) {
            new Metrics(this, settings.getMetricsID());
        }
        if (settings.isVersionChecker()) {
            // TODO Add version checker.
        }
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
