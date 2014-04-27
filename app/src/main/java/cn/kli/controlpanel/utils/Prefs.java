package cn.kli.controlpanel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by carl on 14-4-16.
 */
public class Prefs {

    public final static String BOOLEAN_SERVICE_ENABLE = "main_service_enable";

    private static Prefs sInstance;
    private Context mContext;
    private SharedPreferences mPrefs;

    public static Prefs init(Context context){
        if(sInstance == null){
            sInstance = new Prefs(context);
        }
        return sInstance;
    }

    public static Prefs getInstance(){
        return sInstance;
    }

    private Prefs(Context context){
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void set(String key, boolean value){
        SharedPreferences.Editor editor =  mPrefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defValue){
        return mPrefs.getBoolean(key, defValue);
    }
}
