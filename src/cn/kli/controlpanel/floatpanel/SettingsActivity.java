package cn.kli.controlpanel.floatpanel;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.widget.Toast;

import cn.kli.controlpanel.Baidu;
import cn.kli.controlpanel.LightControlActivity;
import cn.kli.controlpanel.MainService;
import cn.kli.controlpanel.OneKeyLockScreen;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.ThemeSetting;
import cn.kli.controlpanel.R.drawable;
import cn.kli.controlpanel.R.string;
import cn.kli.controlpanel.R.xml;
import cn.kli.utils.klilog;

import com.baidu.mobstat.StatService;

public class SettingsActivity extends PreferenceActivity implements
	OnSharedPreferenceChangeListener,OnPreferenceClickListener {
	
	public final static String KEY_PREF_NOTIFICATION = "key_notification";
	private final static String KEY_PREF_THEME = "key_theme";
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.float_panel_settings);
        PreferenceManager.getDefaultSharedPreferences(this)
        	.registerOnSharedPreferenceChangeListener(this);
        findPreference(KEY_PREF_THEME).setOnPreferenceClickListener(this);
        updateNotification();
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