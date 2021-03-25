package com.Venom.VenomCore.Plugin;

/**
 * @author Alp Beji
 * A class for plugin settings such as metrics.
 */
public class PluginSettings {
    private boolean versionChecker = true;
    private int metricsID = -1;

    public void setVersionChecker(boolean check) {
        this.versionChecker = check;
    }

    public void enableMetrics(int id) {
        this.metricsID = id;
    }

    public boolean isVersionChecker() {
        return versionChecker;
    }

    public boolean isMetrics() {
        return metricsID != -1;
    }

    public int getMetricsID() {
        return metricsID;
    }
}
