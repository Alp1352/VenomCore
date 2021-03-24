package com.venom.nms.common.Chat;

import com.venom.nms.core.Chat.ActionBarCore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBar implements ActionBarCore {
    @Override
    public void sendActionBar(Player p, String text) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', text)));
    }

    @Override
    public void sendActionBar(String text) {
        Bukkit.getOnlinePlayers().forEach(player -> sendActionBar(player, text));
    }

    @Override
    public void clearActionBar(Player p) {
        sendActionBar(p, " ");
    }

    @Override
    public void clearActionBar() {
        Bukkit.getOnlinePlayers().forEach(this::clearActionBar);
    }
}
