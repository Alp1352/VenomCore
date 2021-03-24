package com.Venom.VenomCore.Menu.Internal.Item.Action;
import com.Venom.VenomCore.Menu.GUI;
import org.bukkit.entity.Player;

public class ActionDetails {
    private final Player player;
    private boolean itemForClickCancelled = false;
    private final GUI gui;

    public ActionDetails(Player player, GUI gui) {
        this.player = player;
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isItemForClickCancelled() {
        return itemForClickCancelled;
    }

    public void setItemForClickCancelled(boolean itemForClickCancelled) {
        this.itemForClickCancelled = itemForClickCancelled;
    }

}
