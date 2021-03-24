package com.venom.nms.core.Hologram;

public enum Time {
    DAY,
    HOUR,
    MINUTE,
    SECOND;

    public String getPlaceholder() {
        return "%" + name().toLowerCase() + "%";
    }
}
