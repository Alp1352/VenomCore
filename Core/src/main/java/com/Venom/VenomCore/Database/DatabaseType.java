package com.Venom.VenomCore.Database;

public enum DatabaseType {
    YAML,
    JSON,
    MYSQL,
    SQLITE;


    @Override
    public String toString() {
        return this.name();
    }

    public String getFileExtension() {
        switch (this) {
            case YAML:
                return "yml";
            case JSON:
                return "json";
            default:
                return null;
        }
    }
}


