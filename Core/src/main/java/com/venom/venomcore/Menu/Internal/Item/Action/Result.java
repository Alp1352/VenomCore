package com.venom.venomcore.Menu.Internal.Item.Action;

public enum Result {
    DENY_CLICK(true),
    ALLOW_CLICK(false);

    private final boolean cancel;
    Result(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancelled() {
        return cancel;
    }
}
