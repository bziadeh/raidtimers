package com.cloth.raids;

import com.cloth.Config;
import com.cloth.RaidApi;
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
            long minutesElapsed = (System.currentTimeMillis() - raid.getLastExplosion()) / 1000 / 60;
            if(minutesElapsed >= config.EXPLOSION_THRESHOLD) {
                api.removeRaid(raid);
                api.setShield(raid.getDefender());
            }
        }
    }
}
