package cn.kli.controlpanel;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPrefs {
	private final static String SETTING_PREFS = "setting_prefs";
	private static SettingPrefs sInstance;
	private SharedPreferences mPrefs;
	private SettingPrefs(Context context){
		mPrefs = context.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
	}
	
	public static SettingPrefs init(Context context){
		sInstance = new SettingPrefs(context);
		return sInstance;
	}
	
	public static SettingPrefs getInstance(){
		return sInstance;
	}

	public static SettingPrefs getInstance(Context context){
		if(sInstance == null){
			init(context);
		}
		return sInstance;
	}
	
	public boolean getDeviceAdminEnable(){
		boolean res = mPrefs.getBoolean("device_admin_enable", false);
		return res;
	}
	
	public void setDeviceAdminEnable(boolean enable){
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean("device_admin_enable", enable);
		editor.commit();
	}
}
