package com.venom.venomcore.plugin.external.jobs;

import com.venom.venomcore.plugin.external.Hook;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class JobsHook implements Hook {
    public abstract List<String> getJobs();

    public abstract JobsPlayerHook getJobsPlayerHook(Player p);
}
