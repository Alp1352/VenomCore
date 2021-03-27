package com.venom.venomcore.plugin.database;

public enum DatabaseType {
    YAML("yml"),
    JSON("json"),
    MYSQL,
    SQLITE("db");

    private final String extension;

    DatabaseType(String extension) {
        this.extension = extension;
    }

    DatabaseType() {
        this(null);
    }

    public String getFileExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return this.name();
    }
}


