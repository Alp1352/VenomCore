package com.venom.venomcore.plugin.database.types.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public abstract class ConnectionDatabase {
    private final HikariConfig config;
    private final DataSource source;
    public ConnectionDatabase(HikariConfig config) {
        this.config = config;
        this.source = new HikariDataSource(config);
    }

    public HikariConfig getHikariConfig() {
        return config;
    }

    public DataSource getDataSource() {
        return source;
    }
}
