package com.Venom.VenomCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand extends com.Venom.VenomCore.Commands.Command {
    private final String subCommand;
    private final String[] split;

    public SubCommand(String subCommand, String permission) {
        super(permission);
        this.subCommand = subCommand.trim();
        this.split = this.subCommand.split("\\s+");
    }

    public abstract boolean execute(CommandSender sender, Command command, String[] args);

    public String getSubCommand() {
        return subCommand;
    }

    public String[] getSplit() {
        return split;
    }
}
