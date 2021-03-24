package com.venom.nms.v1_8_R2.Chat;

import com.venom.nms.core.Chat.ActionBarCore;
import com.venom.nms.v1_8_R2.NMSUtils;
import net.minecraft.server.v1_8_R2.ChatComponentText;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBar implements ActionBarCore {
    @Override
    public void sendActionBar(Player p, String text) {
        IChatBaseComponent chatText = new ChatComponentText(text);
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
