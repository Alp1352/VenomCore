package com.venom.venomcore.plugin.commands.fix;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TimingsFixCommand implements CommandExecutor {
    private final TimingsFix fix;
    public TimingsFixCommand(TimingsFix fix) {
        this.fix = fix;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        fix.executeSpigotTimings(sender, args);
        return true;
    }
}
