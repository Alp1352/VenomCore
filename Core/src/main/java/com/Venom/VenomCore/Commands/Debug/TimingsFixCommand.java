package com.Venom.VenomCore.Commands.Debug;

import com.Venom.VenomCore.VenomCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TimingsFixCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        VenomCore.timingsFix.executeSpigotTimings(sender, args);
        return true;
    }
}
