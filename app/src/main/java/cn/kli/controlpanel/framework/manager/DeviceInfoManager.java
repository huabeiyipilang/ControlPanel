package cn.kli.controlpanel.framework.manager;

import android.content.Context;

/**
 * Created by carl on 14-5-2.
 */
public class DeviceInfoManager {
    private static DeviceInfoManager sInstance;

    private Context mContext;

    private DeviceInfoManager(Context context){
        mContext = context;
    }

    public static DeviceInfoManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new DeviceInfoManager(context);
        }
        return sInstance;
    }

    public int getScreenWidth(){
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight(){
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }
}
