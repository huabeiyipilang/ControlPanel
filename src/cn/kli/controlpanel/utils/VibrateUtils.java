package cn.kli.controlpanel.utils;

import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.settings.SettingsWindow;
import android.content.Context;
import android.os.Vibrator;

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
        if(Prefs.getPrefs(mContext).getBoolean(SettingsWindow.SETTING_MENU_VIBRATE, false)){
            mVibrator.vibrate(LENGTH_SHORT);
        }
    }
    
}
