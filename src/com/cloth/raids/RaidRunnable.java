package com.cloth.raids;

import com.cloth.config.Config;
import com.cloth.api.RaidApi;
import com.cloth.RaidTimers;

import java.util.List;

public class RaidRunnable implements Runnable {

    private final RaidApi api = RaidTimers.getApi();
    private final Config config = RaidTimers.getLocalConfig();

    @Override
    public void run() {
        List<Raid> raidList = api.getRaidList();
        for(int i = raidList.size() - 1; i >= 0; i--) {
            final Raid raid = raidList.get(i);
            long elapsed = (System.currentTimeMillis() - raid.getLastExplosion()) / 1000 / 60;
            if(elapsed >= (long) config.EXPLOSION_THRESHOLD) {
                api.removeRaid(raid);
                api.setShield(raid.getDefender());
            }
        }
    }
}
