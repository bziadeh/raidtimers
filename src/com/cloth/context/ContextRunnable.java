package com.cloth.context;

import com.cloth.Config;
import com.cloth.RaidApi;
import com.cloth.RaidTimers;

import java.util.List;

public class ContextRunnable implements Runnable {

    private final RaidApi api;

    public ContextRunnable() {
        api = RaidTimers.getInstance().getApi();
    }

    @Override
    public void run() {
        Config config = RaidTimers.getInstance().getLocalConfig();
        List<RaidContext> contextList = api.getContextList();
        for(int i = contextList.size() - 1; i >= 0; i--) {
            final RaidContext context = contextList.get(i);
            long minutesElapsed = (System.currentTimeMillis() - context.getLastExplosion()) / 1000 / 60;
            if(minutesElapsed >= config.EXPLOSION_THRESHOLD) {
                api.removeRaid(context);
                api.setShield(context.getDefender());
            }
        }
    }
}
