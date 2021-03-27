package com.venom.venomcore.nms.common.chat;

import com.venom.venomcore.nms.core.chat.TitleCore;
import org.bukkit.entity.Player;

public class Title implements TitleCore {

    @Override
    public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendTitle(Player p, String title, String subtitle) {
        sendTitle(p, title, subtitle, 5, 35, 5);
    }

    @Override
    public void clearTitle(Player p) {
        sendTitle(p, null, null, 0, 0,0);
    }
}
