package cn.kli.controlpanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import cn.kli.utils.klilog;

import com.baidu.mobstat.StatService;

public class Prefs {
	//��Ļ���
	public static final String PREF_SCREEN_WIDTH = "screen_width";
	public static final String PREF_SCREEN_HEIGHT = "screen_height";

	//ָʾ��λ�ü�¼
	public static final String PREF_INDICATOR_X = "indicator_x";
	public static final String PREF_INDICATOR_Y = "indicator_y";
	
	public static final String PREF_GUIDE_SHOW = "guide_show";
	
	//״̬���߶�
	public static final String PREF_STATUSBAR_HEIGHT = "statusbar_height";
	
	//第一次记入应用
	public static final String BOOLEAN_FIRST_OPEN = "first_open";
	//状态栏图标显示网速
    public static final String SETTING_NOTIFI_NETWORK_SPEED = "setting_notifi_network_speed";
    //浮动菜单震动反馈
    public static final String SETTING_MENU_VIBRATE = "setting_menu_vibrate";
	
	private final static String SETTING_PREFS = "setting_prefs";
	private static Prefs sInstance;
	private SharedPreferences mPrefs;
	private Context mContext;
	private Prefs(Context context){
		mContext = context;
		mPrefs = mContext.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
		if(isFirstOpen()){
		    Editor editor = mPrefs.edit();
		    editor.putBoolean(BOOLEAN_FIRST_OPEN, false);
		    editor.putBoolean(SETTING_NOTIFI_NETWORK_SPEED, true);
            editor.putBoolean(SETTING_MENU_VIBRATE, true);
		    editor.commit();
		}
	}
	
	public static Prefs init(Context context){
        if(sInstance == null){
            sInstance = new Prefs(context);
        }
		return sInstance;
	}
	
	public static Prefs getInstance(){
		return sInstance;
	}
	
	public static SharedPreferences getPrefs(){
	    return getInstance().mPrefs;
	}
	
	public boolean getDeviceAdminEnable(){
		boolean res = mPrefs.getBoolean("device_admin_enable", false);
		return res;
	}
	
	public void setDeviceAdminEnable(boolean enable){
		StatService.onEvent(mContext, Baidu.EVENT_LOCK_SCREEN, Baidu.DEVICE_ADMIN+enable);
		klilog.info("device admin changed  value = "+enable);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean("device_admin_enable", enable);
		editor.commit();
	}
	
	public boolean isFirstOpen(){
	    return mPrefs.getBoolean(BOOLEAN_FIRST_OPEN, true);
	}
}
