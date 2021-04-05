package com.venom.venomcore.plugin.external.jobs.types.JobsReborn;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.venom.venomcore.plugin.external.jobs.JobsHook;
import com.venom.venomcore.plugin.external.jobs.JobsPlayerHook;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class JobsRebornHook extends JobsHook {
    @Override
    public List<String> getAll() {
        return Jobs.getJobs().stream()
                .map(Job::getName)
                .collect(Collectors.toList());
    }

    @Override
    public JobsPlayerHook getJobsPlayerHook(Player p) {
        return new JobsRebornPlayerHook(p);
    }


    @Override
    public String getName() {
        return "Jobs";
    }
}
