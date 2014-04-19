package cn.kli.controlpanel.manager;

import java.text.DecimalFormat;

import cn.kli.controlpanel.R;

import android.net.TrafficStats;

public class NetworkManager {
    private long oldTotalBytes = 0L;
    private long oldTime;

    public String getDisplay() {
        float speed = getSpeed();
        return formatSpeed(speed);
    }
    
    public int getIcon(){
        float speed = getSpeed();
        int resId = R.drawable.wkb000;
        if(speed < 1000){   // b/s
        }else if(speed < 1000000){  // kb/s
            resId += (int)(speed/1000);
        }else if(speed < 1000000000){   // mb/s
            resId = R.drawable.wmb010 + (int)(speed/100000) - 10;
        }
        return resId;
    }
    
    private float getSpeed(){
        long newTotalBytes = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        long newTime = System.currentTimeMillis();
        float speed = 0;
        try {
            speed = (newTotalBytes - oldTotalBytes) * 1000/(newTime - oldTime); // b/s
        } catch (Exception e) {

        }
        oldTotalBytes = newTotalBytes;
        oldTime = newTime;
        return speed;
    }

    private String formatSpeed(float speed){
        String res = "";
        if(speed < 1000){   // b/s
            res = "B/s";
        }else if(speed < 1000000){  // kb/s
            speed = speed/1000;
            res = "K/s";
        }else if(speed < 1000000000){   // mb/s
            speed = speed/1000000;
            res = "M/s";
        }
        DecimalFormat df = new DecimalFormat("0");
        res = df.format(speed) + res;
        return res;
    }
}
