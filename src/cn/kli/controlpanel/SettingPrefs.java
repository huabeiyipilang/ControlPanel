package cn.kli.controlpanel;

import com.baidu.mobstat.StatService;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPrefs {
	private final static String SETTING_PREFS = "setting_prefs";
	private static SettingPrefs sInstance;
	private SharedPreferences mPrefs;
	private Context mContext;
	private SettingPrefs(Context context){
		mContext = context;
		mPrefs = mContext.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
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
		StatService.onEvent(mContext, Baidu.EVENT_LOCK_SCREEN, Baidu.DEVICE_ADMIN+enable);
		klilog.i("device admin changed  value = "+enable);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean("device_admin_enable", enable);
		editor.commit();
	}
}
