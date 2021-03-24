package com.Venom.VenomCore.Database.Types.Connection;

import com.zaxxer.hikari.HikariConfig;

import java.io.File;

public class SQLiteDatabase extends ConnectionDatabase {
    public SQLiteDatabase(File dataFile) {
        super(getHikariConfig(dataFile));
    }

    private static HikariConfig getHikariConfig(File dataFile) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + dataFile.getAbsolutePath());
        config.setConnectionTestQuery("SELECT 1");

        return config;
    }
}
