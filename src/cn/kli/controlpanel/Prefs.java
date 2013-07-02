package cn.kli.controlpanel;

import cn.kli.utils.klilog;

import com.baidu.mobstat.StatService;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	public static final String PREF_SCREEN_WIDTH = "screen_width";
	public static final String PREF_SCREEN_HEIGHT = "screen_height";
	
	private final static String SETTING_PREFS = "setting_prefs";
	private static Prefs sInstance;
	private SharedPreferences mPrefs;
	private Context mContext;
	private Prefs(Context context){
		mContext = context;
		mPrefs = mContext.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
	}
	
	public static Prefs init(Context context){
		sInstance = new Prefs(context);
		return sInstance;
	}
	
	public static Prefs getInstance(){
		return sInstance;
	}

	public static Prefs getInstance(Context context){
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
