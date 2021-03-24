package com.venom.nms.v1_10_R1.Chat;

import com.venom.nms.core.Chat.TitleCore;
import com.venom.nms.v1_10_R1.NMSUtils;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Title implements TitleCore {

    @Override
    public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title == null && subtitle == null)
            return;

        EntityPlayer player =  NMSUtils.toNMS(p);
        String sendTitle;
        String sendSubtitle;

        if (title != null) {
            sendTitle = title;
        } else {
            sendTitle = " ";
        }

        if (subtitle != null) {
            sendSubtitle = subtitle;
        } else {
            sendSubtitle = " ";
        }

        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', sendTitle) + "\"}");
        PacketPlayOutTitle mainTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        NMSUtils.sendPacket(player, mainTitle);

        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', sendSubtitle) + "\"}");
        PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
        NMSUtils.sendPacket(player, subTitle);

        PacketPlayOutTitle length = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        NMSUtils.sendPacket(player, length);
    }

    @Override
    public void sendTitle(Player p, String title, String subtitle) {
        sendTitle(p, title, subtitle, 5, 35, 5);
    }

    @Override
    public void clearTitle(Player p) {
        EntityPlayer player = NMSUtils.toNMS(p);
        PacketPlayOutTitle clear = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null, -1, -1, -1);

        NMSUtils.sendPacket(player, clear);
    }
}
