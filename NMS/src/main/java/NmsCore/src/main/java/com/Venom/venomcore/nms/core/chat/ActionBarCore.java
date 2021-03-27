package com.venom.venomcore.nms.core.chat;

import org.bukkit.entity.Player;

public interface ActionBarCore {

    void sendActionBar(Player p, String text);

    void sendActionBar(String text);

    void clearActionBar(Player p);

    void clearActionBar();
}
