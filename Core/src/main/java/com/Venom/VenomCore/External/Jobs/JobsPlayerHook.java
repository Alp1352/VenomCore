package com.Venom.VenomCore.External.Jobs;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class JobsPlayerHook {
    private final Player player;
    public JobsPlayerHook(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Makes the player to leave a job.
     * @param job The job to leave.
     */
    public abstract void leaveJob(String job);

    /**
     * Makes the player join a job.
     * @param job The job to join.
     */
    public abstract void joinJob(String job);

    /**
     * Makes the player leave all jobs.
     */
    public abstract void leaveAllJobs();

    /**
     * Get all jobs the player is in.
     * @return All the jobs.
     */
    public abstract List<String> getPlayerJobs();

    /**
     * Demotes player in job.
     * @param job The job.
     */
    public abstract void demoteJob(String job);

    /**
     * Demotes player in job.
     * @param job The job.
     * @param level The amount of level to demote.
     */
    public abstract void demoteJob(String job, int level);

    /**
     * Registers a EAT action for this player.
     * @param item The eaten item.
     */
    public abstract void eatItem(ItemStack item);

    /**
     * Registers a BREAK action for this player.
     * @param block The broken block.
     */
    public abstract void breakBlock(Block block);

    /**
     * Registers a TNTBREAK action for this player.
     * @param block The broken block.
     */
    public abstract void tntBreakBlock(Block block);

    /**
     * Registers a PLACE action for this player.
     * @param block The broken block.
     */
    public abstract void placeBlock(Block block);

    /**
     * Registers a PLACE Entity action for this player.
     * @param entity The placed entity.
     */
    public abstract void placeEntity(Entity entity);

    /**
     * Registers a BREAK Entity action for this player.
     * @param entity The placed entity.
     */
    public abstract void breakEntity(Entity entity);

    /**
     * Registers a BREED Entity action for this player.
     * @param entity The placed entity.
     */
    public abstract void breedEntity(LivingEntity entity);

    /**
     * Registers a KILL Entity action for this player.
     * @param entity The killed entity.
     */
    public abstract void killEntity(LivingEntity entity);

    /**
     * Registers a TAME Entity action for this player.
     * @param entity The tamed entity.
     */
    public abstract void tameEntity(LivingEntity entity);

    /**
     * Registers a CATCH Fish action for this player.
     * @param items The caught fish.
     */
    public abstract void catchFish(ItemStack items);

    /**
     * Registers a KILL Entity action for this player.
     * @param entity The killed entity.
     * @param damageSource The damage source.
     */
    public abstract void killEntity(LivingEntity entity, Entity damageSource);

    /**
     * Registers a ENCHANT action for this player.
     * @param name The name of the enchant.
     * @param level The level of the enchant.
     */
    public abstract void itemEnchanted(String name, int level);

}
