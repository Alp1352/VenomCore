package com.venom.venomcore.plugin.commands;

import com.venom.venomcore.plugin.chat.Color;

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
