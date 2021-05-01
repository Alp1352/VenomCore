package com.venom.venomcore.plugin.language;

import com.google.common.collect.ImmutableMap;
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
    private static final ImmutableMap<String, String> BASE64_CODES;

    private final File file;
    private final VenomPlugin plugin;
    private final String localeName;
    private final Yaml yaml;
    private final java.util.Locale locale;
    private final LocaleType type;

    static {
        BASE64_CODES = ImmutableMap.<String, String>builder()
                // Turkish
                .put("tr_TR","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJiZWFmNTJlMWM0YmZjZDhhMWY0YzY5MTMyMzRiODQwMjQxYWE0ODgyOWMxNWFiYzZmZjhmZGY5MmNkODllIn19fQ==")
                // English
                .put("en_US", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19")
                // French
                .put("fr_FR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNjlhMDY3ZWUzN2U2MzYzNWNhMWU3MjNiNjc2ZjEzOWRjMmRiZGRmZjk2YmJmZWY5OWQ4YjM1Yzk5NmJjIn19fQ==")
                // German
                .put("de_DE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==")
                // Spanish
                .put("es_ES", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0=")
                // Italian
                .put("it_IT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODVjZTg5MjIzZmE0MmZlMDZhZDY1ZDhkNDRjYTQxMmFlODk5YzgzMTMwOWQ2ODkyNGRmZTBkMTQyZmRiZWVhNCJ9fX0=")
                // Japanese
                .put("ja_JP", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY0MGFlNDY2MTYyYTQ3ZDNlZTMzYzQwNzZkZjFjYWI5NmYxMTg2MGYwN2VkYjFmMDgzMmM1MjVhOWUzMzMyMyJ9fX0=")
                // Chinese
                .put("zh_CN", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5YmMwMzVjZGM4MGYxYWI1ZTExOThmMjlmM2FkM2ZkZDJiNDJkOWE2OWFlYjY0ZGU5OTA2ODE4MDBiOThkYyJ9fX0=")
                // Russian
                .put("ru_RU", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZlYWZlZjk4MGQ2MTE3ZGFiZTg5ODJhYzRiNDUwOTg4N2UyYzQ2MjFmNmE4ZmU1YzliNzM1YTgzZDc3NWFkIn19fQ==")
                // Polish
                .put("pl-PL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTIxYjJhZjhkMjMyMjI4MmZjZTRhMWFhNGYyNTdhNTJiNjhlMjdlYjMzNGY0YTE4MWZkOTc2YmFlNmQ4ZWIifX19")
                // Bulgarian
                .put("bg_BG","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkwMzllMWZkODhjNzhkOWQ3YWRjNWFhZDVhYjE2ZTM1NmJlMTM0NjQ5MzRlZDllMmIwY2VmMjA1MWM1YjUzNCJ9fX0=")
                // Romanian
                .put("ro_RO", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNlYjE3MDhkNTQwNGVmMzI2MTAzZTdiNjA1NTljOTE3OGYzZGNlNzI5MDA3YWM5YTBiNDk4YmRlYmU0NjEwNyJ9fX0=")
                // Norwegian
                .put("nb_NO", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRhMDQ4YmMxNTNiMzg0NjdlNzZhMzM0N2YzODM5Njg2MGE4YmM2ODYwMzkzMWU5MWY3YWY1OGJlYzU3MzgzZCJ9fX0=")
                // Swedish
                .put("sv_SE", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI4NWY4MTE0ZGVhOTNjZGVlMDFhYjhmZTVjZmNhMDljOTg0YzI0NTk3NzZiZjYyNmUzNDk1MDM3MDJmMjFlYiJ9fX0=")
                // Brazilian
                .put("pt_BR", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0NjQ3NWQ1ZGNjODE1ZjZjNWYyODU5ZWRiYjEwNjExZjNlODYxYzBlYjE0ZjA4ODE2MWIzYzBjY2IyYjBkOSJ9fX0=")
                // Portuguese
                .put("pt_PT", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWJkNTFmNDY5M2FmMTc0ZTZmZTE5NzkyMzNkMjNhNDBiYjk4NzM5OGUzODkxNjY1ZmFmZDJiYTU2N2I1YTUzYSJ9fX0=")
                .build();
    }

    public Locale(VenomPlugin plugin, LocaleType type, File file, String localeName) {
        this.plugin = plugin;
        this.file = file;
        this.type = type;
        this.localeName = withExtension(localeName);
        this.yaml = LightningBuilder.fromFile(file)
                    .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                    .setReloadSettings(ReloadSettings.MANUALLY)
                    .setDataType(DataType.SORTED)
                    .createYaml();

        String[] split = localeName.split("_");
        this.locale = new java.util.Locale(split[0], split[1]);
    }

    /**
     * Creates the locale folders.
     * @param plugin The plugin that is creating the folders.
     * @return True if the folder was successfully created.
     */
    public static boolean createFolder(VenomPlugin plugin) {
        return getFolder(plugin, LocaleType.LANGUAGE).mkdirs() && getFolder(plugin, LocaleType.MENU).mkdirs();
    }

    /**
     * Copies a locale from resources folder.
     * @param plugin The plugin responsible.
     * @param inputStream The input stream.
     * @param localeName The locale name i.e. en_US.
     */
    public static void copyLocale(VenomPlugin plugin, LocaleType type, InputStream inputStream, String localeName) {
        File localeFile = new File(getFolder(plugin, type), withExtension(localeName));
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
    public static void copyLocales(VenomPlugin plugin, LocaleType type, String... localeNames) {
        for (String name : localeNames) {
            String localeName = withExtension(name);
            InputStream stream = plugin.getResource("locales" + File.separator + type.get() + File.separator + localeName);
            copyLocale(plugin, type, stream, localeName);
        }
    }

    public static void copyAllLocales(VenomPlugin plugin, LocaleType type) {
        URL url = plugin
                .getClass()
                .getClassLoader()
                .getResource("locales" + File.separator + type.get());

        if (url == null) return;

        String directoryPath = url.getFile();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        for (File file : files) {
            String name = FilenameUtils.removeExtension(file.getName());
            copyLocales(plugin, type, name);
        }
    }

    /**
     * Loads a locale from plugins data folder.
     * @param plugin The plugin responsible.
     * @param localeName The locales name.
     * @return The loaded locale.
     */
    public static Locale loadLocale(VenomPlugin plugin, LocaleType type, String localeName) {
        String name = withExtension(localeName);

        File file = new File(getFolder(plugin, type), name);

        Locale locale = new Locale(plugin, type, file, name);
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
    public static Locale loadLocale(VenomPlugin plugin, LocaleType type, java.util.Locale locale) {
        return loadLocale(plugin,  type,locale.getLanguage() + "_" + locale.getCountry());
    }

    /**
     * Loads all the locales.
     * @param plugin The plugin.
     * @return All the loaded locales.
     */
    public static List<Locale> loadAllLocales(VenomPlugin plugin, LocaleType type) {
        File folder = getFolder(plugin, type);
        List<Locale> locales = new ArrayList<>();

        File[] files = folder.listFiles(file -> file.getName().endsWith(EXTENSION));
        for (File file : files) {
            Locale locale = new Locale(plugin, type, file, file.getName());
            locale.forceReload();
            locales.add(locale);
        }
        return locales;
    }

    public static String getHeadOf(String localeName) {
        return BASE64_CODES.get(localeName);
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
    private static File getFolder(VenomPlugin plugin, LocaleType type) {
        return new File(plugin.getDataFolder(), File.separator + "locales" + File.separator + type.get());
    }

    public enum LocaleType {
        MENU("menu"),
        LANGUAGE("language");

        private final String suffix;
        LocaleType(String suffix) {
            this.suffix = suffix;
        }

        public String get() {
            return suffix;
        }
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

    public Yaml toYaml() {
        return yaml;
    }

    public java.util.Locale getLocale() {
        return locale;
    }

    public LocaleType getType() {
        return type;
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
