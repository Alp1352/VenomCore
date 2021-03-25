package com.venom.venomcore.External.Jobs;

import com.venom.venomcore.External.Hook;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class JobsHook implements Hook {
    public abstract List<String> getJobs();

    public abstract JobsPlayerHook getJobsPlayerHook(Player p);
}
