package com.Venom.VenomCore.Database.Types.Connection;

import com.Venom.VenomCore.Database.ConnectionSettings;
import com.zaxxer.hikari.HikariConfig;

public class MySQLDatabase extends ConnectionDatabase {
    public MySQLDatabase(ConnectionSettings settings) {
        super(getHikariConfig(settings));
    }

    private static HikariConfig getHikariConfig(ConnectionSettings settings) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + settings.getHost() + ":" + settings.getPort() + "/" + settings.getDatabase());
        config.setUsername(settings.getUser());
        config.setPassword(settings.getPassword());
        config.setMaximumPoolSize(10);

        return config;
    }
}
