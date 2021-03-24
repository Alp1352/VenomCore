package com.venom.nms.core.Chat;

import org.bukkit.entity.Player;

public interface TitleCore {
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    void sendTitle(Player player, String title,  String subtitle);

    void clearTitle(Player player);
}
