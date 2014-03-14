package cn.kli.controlpanel.utils;

import android.content.Context;
import android.os.Vibrator;
import cn.kli.controlpanel.Prefs;

public class VibrateUtils {
    public static final int LENGTH_SHORT = 1;
    private static VibrateUtils sInstance;
    
    private Vibrator mVibrator;
    private Context mContext;
    
    private VibrateUtils(Context context){
        mContext = context;
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    
    public static VibrateUtils getInstance(Context context){
        if(sInstance == null){
            sInstance = new VibrateUtils(context);
        }
        return sInstance;
    }
    
    public void vibrateShortly(){
        if(Prefs.getPrefs().getBoolean(Prefs.SETTING_MENU_VIBRATE, false)){
            mVibrator.vibrate(LENGTH_SHORT);
        }
    }
    
}
