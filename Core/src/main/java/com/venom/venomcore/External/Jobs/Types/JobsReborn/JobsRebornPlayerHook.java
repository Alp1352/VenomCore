package com.venom.venomcore.External.Jobs.Types.JobsReborn;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.actions.EnchantActionInfo;
import com.gamingmesh.jobs.actions.EntityActionInfo;
import com.gamingmesh.jobs.actions.ItemActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.venom.venomcore.External.Jobs.JobsPlayerHook;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class JobsRebornPlayerHook extends JobsPlayerHook {
    private final JobsPlayer jobsPlayer;
    public JobsRebornPlayerHook(Player player) {
        super(player);
        this.jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player.getUniqueId());
    }

    @Override
    public void leaveJob(String job) {
        jobsPlayer.leaveJob(getJob(job));
    }

    @Override
    public void joinJob(String job) {
        jobsPlayer.joinJob(getJob(job));
    }

    @Override
    public void leaveAllJobs() {
        jobsPlayer.leaveAllJobs();
    }

    @Override
    public List<String> getPlayerJobs() {
        return jobsPlayer.getJobProgression().stream().map(jobProgression -> jobProgression.getJob().getName()).collect(Collectors.toList());
    }

    @Override
    public void demoteJob(String job) {
        demoteJob(job, 1);
    }

    @Override
    public void demoteJob(String job, int level) {
        jobsPlayer.demoteJob(getJob(job), level);
    }

    @Override
    public void eatItem(ItemStack item) {
        Jobs.action(jobsPlayer, new ItemActionInfo(item, ActionType.EAT));
    }

    @Override
    public void breakBlock(Block block) {
        Jobs.action(jobsPlayer, new BlockActionInfo(block, ActionType.BREAK));
    }

    @Override
    public void tntBreakBlock(Block block) {
        Jobs.action(jobsPlayer, new BlockActionInfo(block, ActionType.TNTBREAK));
    }

    @Override
    public void placeBlock(Block block) {
        Jobs.action(jobsPlayer, new BlockActionInfo(block, ActionType.PLACE));
    }

    @Override
    public void placeEntity(Entity entity) {
        Jobs.action(jobsPlayer, new EntityActionInfo(entity, ActionType.PLACE));
    }

    @Override
    public void breakEntity(Entity entity) {
        Jobs.action(jobsPlayer, new EntityActionInfo(entity, ActionType.BREAK));
    }

    @Override
    public void breedEntity(LivingEntity entity) {
        Jobs.action(jobsPlayer, new EntityActionInfo(entity, ActionType.BREED));
    }

    @Override
    public void killEntity(LivingEntity entity) {
        Jobs.action(jobsPlayer, new EntityActionInfo(entity, ActionType.KILL));
    }

    @Override
    public void tameEntity(LivingEntity entity) {
        Jobs.action(jobsPlayer, new EntityActionInfo(entity, ActionType.TAME));
    }

    @Override
    public void catchFish(ItemStack items) {
        Jobs.action(jobsPlayer, new ItemActionInfo(items, ActionType.FISH));
    }

    @Override
    public void killEntity(LivingEntity entity, Entity damageSource) {
        Jobs.action(jobsPlayer, new EntityActionInfo(entity, ActionType.KILL), damageSource);
    }

    @Override
    public void itemEnchanted(String enchantName, int level) {
        Jobs.action(jobsPlayer, new EnchantActionInfo(enchantName,level,ActionType.ENCHANT));
    }

    private Job getJob(String jobName) {
        return Jobs.getJob(jobName);
    }
}
