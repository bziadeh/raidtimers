package com.cloth.shield;

import com.cloth.RaidTimers;

import java.util.List;

public class ShieldRunnable implements Runnable {
    @Override
    public void run() {
        List<Shield> shieldList = RaidTimers.getInstance().getApi().getShieldList();
        for(int i = shieldList.size() - 1; i >= 0; i--) {
            final Shield shield = shieldList.get(i);
            long minutesElapsed = (System.currentTimeMillis() - shield.getStartTime()) / 1000 / 60;
            if(minutesElapsed > RaidTimers.getInstance().getLocalConfig().SHIELD_DURATION) {
                shieldList.remove(i);
            }
        }
    }
}
