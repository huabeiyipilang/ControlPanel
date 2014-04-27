package cn.kli.controlpanel.framework.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import cn.kli.utils.klilog;

public class BatteryStatusManager{
    protected static int sCount;
    private static BatteryStatusManager sInstance;
    
    private float mValue = 1f;
    
    private Context mContext;

    
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 100);
            klilog.info("battery level:"+level+", scale:"+scale);
            mValue = Float.valueOf(level) / Float.valueOf(scale);
        }
        
    };
    
    private BatteryStatusManager(Context context){
        mContext = context;
        context.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    
    
    public static BatteryStatusManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new BatteryStatusManager(context);
        }
        sCount++;
        return sInstance;
    }
    
    protected void release() {
        sCount--;
        if(sCount <= 0){
            mContext.unregisterReceiver(mReceiver);
            sCount = 0;
            sInstance = null;
        }
    }

    public float getValue() {
        return mValue;
    }
}
