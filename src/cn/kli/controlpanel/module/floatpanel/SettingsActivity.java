package cn.kli.controlpanel.module.floatpanel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import cn.kli.controlpanel.MainService;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.utils.klilog;

import com.baidu.mobstat.StatService;

public class SettingsActivity extends PreferenceActivity implements
	OnSharedPreferenceChangeListener,OnPreferenceClickListener {
	
	public final static String KEY_PREF_NOTIFICATION = "key_notification";
	private final static String KEY_PREF_THEME = "key_theme";
	public final static String KEY_PREF_INDICATOR_SWITCH = "key_indicator_switch";
	public final static String KEY_PREF_INDICATOR_TYPES = "key_indicator_types";
	public final static String KEY_PREF_INDICATOR_LAUNCHER_SWITCH = "key_indicator_only_launcher_switch";
	public final static String KEY_PREF_INDICATOR_AUTO_EDGE = "key_indicator_auto_edge";
	public final static String KEY_PREF_INDICATOR_LOCK = "key_indicator_lock";
	public final static String KEY_PREF_INDICATOR_STATUSBAR_MODE = "key_indicator_statusbar_mode";
	
	private CheckBoxPreference mIndicatorSwitch;
	private ListPreference mIndicatorTypes;
	private CheckBoxPreference mIndicatorLauncherSwitch;
	private CheckBoxPreference mIndicatorAutoEdge;
	private CheckBoxPreference mIndicatorStatusbarMode;
	private CheckBoxPreference mIndicatorLock;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.float_panel_settings);
        PreferenceManager.getDefaultSharedPreferences(this)
        	.registerOnSharedPreferenceChangeListener(this);
        initUI();
        findPreference(KEY_PREF_THEME).setOnPreferenceClickListener(this);
        updateNotification();
        updateIndicatorSettings();
    }
    
    private void initUI(){
    	mIndicatorSwitch = (CheckBoxPreference)findPreference(KEY_PREF_INDICATOR_SWITCH);
    	mIndicatorTypes = (ListPreference)findPreference(KEY_PREF_INDICATOR_TYPES);
    	mIndicatorLauncherSwitch = (CheckBoxPreference)findPreference(KEY_PREF_INDICATOR_LAUNCHER_SWITCH);
    	mIndicatorAutoEdge = (CheckBoxPreference)findPreference(KEY_PREF_INDICATOR_AUTO_EDGE);
    	mIndicatorStatusbarMode = (CheckBoxPreference)findPreference(KEY_PREF_INDICATOR_STATUSBAR_MODE);
    	mIndicatorLock = (CheckBoxPreference)findPreference(KEY_PREF_INDICATOR_LOCK);
    }

	public boolean onPreferenceClick(Preference pref) {
		String key = pref.getKey();
		klilog.i("key = "+key);
		if(key.equals(KEY_PREF_THEME)){
			startActivity(new Intent(this, ThemeSetting.class));
			return true;
		}
		return false;
	}
	
	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		if(key.equals(KEY_PREF_NOTIFICATION)){
			updateNotification();
		}else if(key.equals(KEY_PREF_INDICATOR_SWITCH)){
			updateIndicatorSettings();
		}else if(key.equals(KEY_PREF_INDICATOR_LAUNCHER_SWITCH)){
			updateIndicatorSettings();
		}else if(key.equals(KEY_PREF_INDICATOR_TYPES)){
			updateIndicatorSettings();
		}else if(key.equals(KEY_PREF_INDICATOR_AUTO_EDGE)){
			updateIndicatorSettings();
		}else if(key.equals(KEY_PREF_INDICATOR_STATUSBAR_MODE)){
			updateIndicatorSettings();
		}else if(key.equals(KEY_PREF_INDICATOR_LOCK)){
			updateIndicatorSettings();
		}
	}
	
	private void updateIndicatorSettings(){
		FloatManager manager = FloatManager.getInstance(this);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		//浮动窗开关
		boolean enable = pref.getBoolean(KEY_PREF_INDICATOR_SWITCH, false);
		
		//浮动窗显示类型
		mIndicatorTypes.setEnabled(enable);
		String type = pref.getString(KEY_PREF_INDICATOR_TYPES, null);
		manager.setIndicatorType(type);
		mIndicatorTypes.setSummary(mIndicatorTypes.getEntry());

		//状态栏模式
		mIndicatorStatusbarMode.setEnabled(enable);
		boolean statusbarMode = pref.getBoolean(KEY_PREF_INDICATOR_STATUSBAR_MODE, false);
		if(statusbarMode){
			Editor editor = pref.edit();
			editor.putBoolean(KEY_PREF_INDICATOR_LOCK, true);
			editor.putBoolean(KEY_PREF_INDICATOR_LAUNCHER_SWITCH, false);
			editor.putBoolean(KEY_PREF_INDICATOR_AUTO_EDGE, false);
			editor.commit();
		}
		manager.getIndicator().setStatusbarMode(statusbarMode);
		
		//锁定位置
		mIndicatorLock.setEnabled(enable && !statusbarMode);
		boolean lock = pref.getBoolean(KEY_PREF_INDICATOR_LOCK, false);
		manager.getIndicator().lock(lock);
		
		//仅在桌面显示
		mIndicatorLauncherSwitch.setEnabled(enable && !statusbarMode);
		if(pref.getBoolean(KEY_PREF_INDICATOR_LAUNCHER_SWITCH, false)){
			FloatPanelService.startLauncherCheck(this);
		}else{
			FloatPanelService.stopLauncherCheck(this);
		}
		
		//边缘吸附
		mIndicatorAutoEdge.setEnabled(enable && !statusbarMode);
		if(pref.getBoolean(KEY_PREF_INDICATOR_AUTO_EDGE, true)){
			int x = pref.getInt(Prefs.PREF_SCREEN_WIDTH, 0)/2;
			manager.updateIndicatorLocation(x, manager.getIndicator().getPositionY());
		}
		
		
		//悬浮窗
		if(enable){
			manager.showIndicator();
		}else{
			manager.hideIndicator();
		}
	}
	
	private void updateNotification() {
		sendCmd(MainService.CMD_UPDATE_NOTIFICATION);
	}

	@Override
	protected void onPause() {
		StatService.onPause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();
	}
    
	private void sendCmd(int cmd){
		Intent intent_service = new Intent(this, MainService.class);
		intent_service.putExtra(MainService.SERVICE_CMD, cmd);
		startService(intent_service);
	}

}