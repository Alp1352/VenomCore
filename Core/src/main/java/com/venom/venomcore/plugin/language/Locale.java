package com.venom.venomcore.plugin.language;

import com.venom.venomcore.plugin.plugin.VenomPlugin;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alp Beji
 * @apiNote A class for locale folders.
 */
@SuppressWarnings("unused")
public class Locale {
    private static final String EXTENSION = ".yml";

    private final File file;
    private final VenomPlugin plugin;
    private final String localeName;
    private final Yaml yaml;

    public Locale(VenomPlugin plugin, File file, String localeName) {
        this.plugin = plugin;
        this.file = file;
        this.localeName = withExtension(localeName);
        this.yaml = LightningBuilder.fromFile(file)
                    .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                    .setReloadSettings(ReloadSettings.MANUALLY)
                    .setDataType(DataType.SORTED)
                    .createYaml();
    }

    /**
     * Creates the locale folders.
     * @param plugin The plugin that is creating the folders.
     * @return True if the folder was successfully created.
     */
    public static boolean createFolder(VenomPlugin plugin) {
        return getLocalesFolder(plugin).mkdirs();
    }

    /**
     * Copies a locale from resources folder.
     * @param plugin The plugin responsible.
     * @param inputStream The input stream.
     * @param localeName The locale name i.e. en_US.
     */
    public static void copyLocale(VenomPlugin plugin, InputStream inputStream, String localeName) {
        File localeFile = new File(getLocalesFolder(plugin), withExtension(localeName));
        try {
            FileUtils.copyInputStreamToFile(inputStream, localeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies multiple locales from resource folder.
     * @param plugin The plugin responsible.
     * @param localeNames The names of the locales.
     */
    public static void copyLocales(VenomPlugin plugin, String... localeNames) {
        for (String name : localeNames) {
            String localeName = withExtension(name);
            InputStream stream = plugin.getResource(localeName);
            copyLocale(plugin, stream, localeName);
        }
    }

    public static void copyAllLocales(VenomPlugin plugin) {
        URL url = plugin
                .getClass()
                .getClassLoader()
                .getResource("locales");

        if (url == null) return;

        String directoryPath = url.getFile();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        for (File file : files) {
            String name = FilenameUtils.removeExtension(file.getName());
            copyLocales(plugin, name);
        }
    }

    /**
     * Loads a locale from plugins data folder.
     * @param plugin The plugin responsible.
     * @param localeName The locales name.
     * @return The loaded locale.
     */
    public static Locale loadLocale(VenomPlugin plugin, String localeName) {
        String name = withExtension(localeName);

        File file = new File(getLocalesFolder(plugin), name);

        Locale locale = new Locale(plugin, file, name);
        locale.forceReload();

        plugin.setLocale(locale);
        return locale;
    }

    /**
     * Loads a locale from plugins data folder.
     * @param plugin The plugin.
     * @param locale The locales name.
     * @return The loaded locale.
     */
    public static Locale loadLocale(VenomPlugin plugin, java.util.Locale locale) {
        return loadLocale(plugin,  locale.getLanguage() + "_" + locale.getCountry());
    }

    /**
     * Loads all the locales.
     * @param plugin The plugin.
     * @return All the loaded locales.
     */
    public static List<Locale> loadAllLocales(VenomPlugin plugin) {
        File folder = getLocalesFolder(plugin);
        List<Locale> locales = new ArrayList<>();
        File[] files = folder.listFiles(file -> file.getName().endsWith(EXTENSION));
        for (File file : files) {
            Locale locale = new Locale(plugin, file, file.getName());
            locale.forceReload();
            locales.add(locale);
        }
        return locales;
    }

    /**
     * Gets a message from the locale file.
     * @param key The key.
     * @return The locale message.
     */
    public LocaleMessage getMessage(String key) {
        return new LocaleMessage(yaml.getString(key), yaml.getString("prefix"));
    }

    /**
     * Reloads the locale.
     */
    public void forceReload() {
        if (!file.exists())
            return;

        yaml.forceReload();
    }

    /**
     * Checks if the string has an extension or not.
     * If not, adds the extension.
     * @param string The string to check.
     * @return The string with extension.
     */
    private static String withExtension(String string) {
        return string.endsWith(EXTENSION) ? string : string + EXTENSION;
    }

    /**
     * Gets the locales folder of a plugin.
     * @param plugin The plugin to get the locale folder of.
     * @return The folder.
     */
    private static File getLocalesFolder(VenomPlugin plugin) {
        return new File(plugin.getDataFolder(), File.separator + "locales");
    }


    /**
     * Gets the locale name.
     * @return The locale name i.e en_US
     */
    public String getLocaleName() {
        return localeName;
    }

    /**
     * Gets the plugin.
     * @return The plugin.
     */
    public VenomPlugin getPlugin() {
        return plugin;
    }
}
