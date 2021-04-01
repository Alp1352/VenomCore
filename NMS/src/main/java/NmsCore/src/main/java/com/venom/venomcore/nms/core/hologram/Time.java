package com.venom.venomcore.nms.core.hologram;

public enum Time {
    DAY,
    HOUR,
    MINUTE,
    SECOND;

    public String getPlaceholder() {
        return "%" + name().toLowerCase() + "%";
    }
}
