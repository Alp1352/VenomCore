package com.venom.venomcore.Commands;

import com.venom.venomcore.Chat.Color;

public abstract class Command {
    private String permission;
    private boolean commandPrivate = false;
    private String permErrorMessage = "&d%plugin% » &cBunu yapmaya yetkin yok!";

    public Command(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermErrorMessage() {
        return permErrorMessage;
    }

    public void setPermErrorMessage(String permErrorMessage) {
        this.permErrorMessage = Color.translate(permErrorMessage);
    }

    public boolean isCommandPrivate() {
        return commandPrivate;
    }

    public void setCommandPrivate(boolean commandPrivate) {
        this.commandPrivate = commandPrivate;
    }
}
