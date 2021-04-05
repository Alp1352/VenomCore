package com.venom.venomcore.plugin.external.protection;
import com.venom.venomcore.plugin.external.Hook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class ProtectionHook implements Hook {
    /**
     * Checks if the player can build in the given location.
     * @param player The player to check.
     * @param location The location to check.
     * @return True if the player can place blocks.
     */
    public abstract boolean canPlace(Player player, Location location);

    /**
     * Checks if the player can break the block in the given location.
     * @param player The player to check.
     * @param location The location to check.
     * @return True if the player can break blocks.
     */
    public abstract boolean canBreak(Player player, Location location);

    /**
     * Checks if the player can interact with a block in the given location.
     * @param player The player to check.
     * @param location The location to check.
     * @return True if the player can interact with blocks in the given location.
     */
    public abstract boolean canInteract(Player player, Location location);
}
