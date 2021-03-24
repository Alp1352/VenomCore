package com.Venom.VenomCore.External.Jobs;

import com.Venom.VenomCore.External.Hook;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class JobsHook implements Hook {
    public abstract List<String> getJobs();

    public abstract JobsPlayerHook getJobsPlayerHook(Player p);
}
