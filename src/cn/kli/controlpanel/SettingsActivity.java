package cn.kli.controlpanel;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import com.baidu.mobstat.StatService;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.webkit.WebView;

public class SettingsActivity extends PreferenceActivity implements
	OnSharedPreferenceChangeListener,OnPreferenceClickListener {
	
	public final static String KEY_PREF_NOTIFICATION = "key_notification";
	private final static String KEY_PREF_THEME = "key_theme";
	private final static String KEY_PREF_ABOUT = "key_about";
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.getDefaultSharedPreferences(this)
        	.registerOnSharedPreferenceChangeListener(this);
        findPreference(KEY_PREF_THEME).setOnPreferenceClickListener(this);
        findPreference(KEY_PREF_ABOUT).setOnPreferenceClickListener(this);
        updateNotification();
    }

	public boolean onPreferenceClick(Preference pref) {
		String key = pref.getKey();
		KLog.i("key = "+key);
		if(key.equals(KEY_PREF_THEME)){
			startActivity(new Intent(this, ThemeSetting.class));
			return true;
		}else if(key.equals(KEY_PREF_ABOUT)){
			showAboutDialog();
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

	private void showAboutDialog(){
		StatService.onEvent(this, Baidu.SETTINGS, "show about dialog");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		WebView view = new WebView(this);
		view.loadUrl("file:///android_asset/html/about.html");
		builder.setTitle(R.string.setting_about)
				.setView(view)
				.setPositiveButton(R.string.dialog_close, new OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
				
			});
		KLog.i("dialog show");
		builder.show();
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