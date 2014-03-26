package cn.kli.controlpanel.module.quickpanel;

import android.app.ApplicationErrorReport.BatteryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.devicesmanager.KliBatteryManager;
import cn.kli.utils.DeviceUtils;
import cn.kli.utils.klilog;

public class IndicatorManager {
    public static final int MODE_MEMORY = 1;
    public static final int MODE_CPU = 2;
    public static final int MODE_BATTERY = 3;
    
    private LinearLayout mUpView;
    private LinearLayout mDownView;
    private Policy mPolicy;
    private Context mContext;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
            case 1:
                int up = Math.round(mPolicy.getValue()*100);
                int down = 100 - up;
                LinearLayout.LayoutParams paramsUsed = ((LinearLayout.LayoutParams)mUpView.getLayoutParams());
                paramsUsed.weight = up;
                mUpView.setLayoutParams(paramsUsed);

                LinearLayout.LayoutParams paramsFree = ((LinearLayout.LayoutParams)mDownView.getLayoutParams());
                paramsFree.weight = down;
                mDownView.setLayoutParams(paramsFree);
                
                mHandler.sendEmptyMessageDelayed(1, 3000);
                break;
            }
        }
        
    };
    
    private static IndicatorManager sInstance;
    
    private IndicatorManager(Context context){
        mContext = context;
        setMode(Prefs.getPrefs().getInt(Prefs.SETTING_INDICATOR_MODE, MODE_BATTERY));
    }
    
    public static IndicatorManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new IndicatorManager(context);
            
        }
        return sInstance;
    }
    
    void setViews(LinearLayout v1, LinearLayout v2){
        mUpView = v1;
        mDownView = v2;
    }
    
    void setPolicy(Policy policy){
        mPolicy = policy;
    }
    
    void start(){
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessage(1);
    }
    
    void stop(){
        mHandler.removeMessages(1);
    }
    
    public void setMode(int mode){
        Policy policy = null;
        switch(mode){
        case MODE_BATTERY:
            policy = new BatteryPolicy();
            break;
        case MODE_CPU:
            policy = new CpuPolicy();
            break;
        case MODE_MEMORY:
            policy = new MemoryPolicy();
            break;
        default:
            policy = new BatteryPolicy();
            break;
        }
        if(mPolicy != null){
            mPolicy.release();
        }
        mPolicy = policy;
    }
    
    private abstract class Policy{
        abstract float getValue();
        protected void release(){
            
        }
    }
    
    private class MemoryPolicy extends Policy{
        
        public float getValue() {
            double availMem = DeviceUtils.getAvailMemory(mContext);
            double totalMem = DeviceUtils.getTotalMemory();
            return (float) ((totalMem - availMem)/totalMem);
        }

    }

    private class CpuPolicy extends Policy{
        
        public float getValue() {
            return 0f;
        }
        
    }

    private class BatteryPolicy extends Policy{
        
        public BatteryPolicy() {
            super();
        }

        public float getValue() {
            return KliBatteryManager.getInstance(mContext).getValue();
        }
    
    }
}
