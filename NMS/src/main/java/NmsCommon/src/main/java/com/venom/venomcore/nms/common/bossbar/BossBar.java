package com.venom.venomcore.nms.common.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

/**
 * @author Alp Beji
 * @apiNote Because withers start showing particles below 50 percent health,
 * @apiNote percentage below 50 is not supported.
 * A class for sending BossBars in 1.8.8.
 */
public class BossBar implements com.venom.venomcore.nms.core.bossbar.BossBar {
    private final org.bukkit.boss.BossBar bossBar;
    public BossBar(String text, double percentage, BarColor color, BarStyle style) {
        bossBar = Bukkit.createBossBar(text, color, style);
        bossBar.setProgress(percentage / 100);
        bossBar.setVisible(true);
    }

    public void show() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    public void show(Player p) {
        bossBar.addPlayer(p);
    }

    public void update(String text, double percentage) {
        bossBar.setTitle(text);
        bossBar.setProgress(percentage / 100);
    }

    public void hide() {
        bossBar.removeAll();
    }

    public void hide(Player p) {
        bossBar.removePlayer(p);
    }

    @Override
    public void update(String text) {
        bossBar.setTitle(text);
    }

    @Override
    public void update(double percentage) {
        bossBar.setProgress(percentage / 100);
    }

    @Override
    public double getPercentage() {
        return bossBar.getProgress() * 100;
    }

    @Override
    public String getText() {
        return bossBar.getTitle();
    }
}
