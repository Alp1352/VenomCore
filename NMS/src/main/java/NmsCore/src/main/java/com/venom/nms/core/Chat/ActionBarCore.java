package com.venom.nms.core.Chat;

import org.bukkit.entity.Player;

public interface ActionBarCore {

    void sendActionBar(Player p, String text);

    void sendActionBar(String text);

    void clearActionBar(Player p);

    void clearActionBar();
}
