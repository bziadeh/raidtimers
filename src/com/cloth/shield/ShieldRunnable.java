package com.cloth.shield;

import com.cloth.RaidTimers;
import com.cloth.api.RaidApi;
import com.cloth.config.Config;

import java.util.List;

public class ShieldRunnable implements Runnable {

    private final RaidApi api = RaidTimers.getApi();
    private final Config config = RaidTimers.getLocalConfig();

    @Override
    public void run() {
        List<Shield> shieldList = api.getShieldList();
        for(int i = shieldList.size() - 1; i >= 0; i--) {
            final Shield shield = shieldList.get(i);
            long elapsed = (System.currentTimeMillis() - shield.getStartTime()) / 1000 / 60;
            if(elapsed >= (long) config.SHIELD_DURATION) {
                shieldList.remove(i);
            }
        }
    }
}
