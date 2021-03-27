package com.venom.venomcore.nms.v1_8_R3.chat;

import com.venom.venomcore.nms.core.chat.ActionBarCore;
import com.venom.venomcore.nms.v1_8_R3.NMSUtils;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBar implements ActionBarCore {
    @Override
    public void sendActionBar(Player p, String text) {
        IChatBaseComponent chatText = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatText, (byte) 2);
        NMSUtils.sendPacket(NMSUtils.toNMS(p), packet);
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
