package cn.kli.controlpanel.floatpanel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import cn.kli.controlpanel.MainService;
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
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.float_panel_settings);
        PreferenceManager.getDefaultSharedPreferences(this)
        	.registerOnSharedPreferenceChangeListener(this);
        findPreference(KEY_PREF_THEME).setOnPreferenceClickListener(this);
        updateNotification();
        updateIndicatorSettings();
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
			if(pref.getBoolean(KEY_PREF_INDICATOR_LAUNCHER_SWITCH, false)){
				FloatPanelService.startLauncherCheck(this);
			}else{
				FloatPanelService.stopLauncherCheck(this);
			}
		}else if(key.equals(KEY_PREF_INDICATOR_TYPES)){
			String type = pref.getString(KEY_PREF_INDICATOR_TYPES, null);
			FloatManager.getInstance(this).setIndicatorType(type);
		}
	}
	
	private void updateIndicatorSettings(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean enable = pref.getBoolean(KEY_PREF_INDICATOR_SWITCH, false);
		findPreference(KEY_PREF_INDICATOR_TYPES).setEnabled(enable);
		findPreference(KEY_PREF_INDICATOR_LAUNCHER_SWITCH).setEnabled(enable);
		if(enable){
			FloatManager.getInstance(this).showIndicator();
		}else{
			FloatManager.getInstance(this).hideIndicator();
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