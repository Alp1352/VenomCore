package com.venom.venomcore.Database;
import com.venom.venomcore.Plugin.VenomPlugin;

public abstract class VenomDatabase {
    private final VenomPlugin plugin;
    private final DatabaseType type;

    public VenomDatabase(VenomPlugin plugin, DatabaseType type) {
        this.plugin = plugin;
        this.type = type;
    }

    public DatabaseType getType() {
        return type;
    }

    public VenomPlugin getPlugin() {
        return plugin;
    }

    public abstract void load();
}
