package cn.kli.controlpanel;

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

import com.baidu.mobstat.StatService;

public class SettingsActivity extends PreferenceActivity implements
	OnSharedPreferenceChangeListener,OnPreferenceClickListener {
	
	public final static String KEY_PREF_NOTIFICATION = "key_notification";
	private final static String KEY_PREF_THEME = "key_theme";
	private final static String KEY_PREF_LOCKSCREEN = "key_lockscreen";
	private final static String KEY_PREF_LIGHTCONTROL = "key_lightcontrol";
	private final static String KEY_PREF_ABOUT = "key_about";
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.getDefaultSharedPreferences(this)
        	.registerOnSharedPreferenceChangeListener(this);
        findPreference(KEY_PREF_THEME).setOnPreferenceClickListener(this);
        findPreference(KEY_PREF_ABOUT).setOnPreferenceClickListener(this);
        findPreference(KEY_PREF_LOCKSCREEN).setOnPreferenceClickListener(this);
        findPreference(KEY_PREF_LIGHTCONTROL).setOnPreferenceClickListener(this);
        updateNotification();
    }

	public boolean onPreferenceClick(Preference pref) {
		String key = pref.getKey();
		klilog.i("key = "+key);
		if(key.equals(KEY_PREF_THEME)){
			startActivity(new Intent(this, ThemeSetting.class));
			return true;
		}else if(key.equals(KEY_PREF_ABOUT)){
			showAboutDialog();
			return true;
		}else if(key.equals(KEY_PREF_LOCKSCREEN)){
			StatService.onEvent(this, Baidu.EVENT_LOCK_SCREEN, Baidu.ADD_SHORTCUTS);
	        Intent intent = new Intent(this, OneKeyLockScreen.class);
			addShortcut(this, intent, R.string.setting_lockscreen, R.drawable.lock);
			Toast.makeText(this, R.string.lockscreen_added_toast, Toast.LENGTH_LONG).show();
			return true;
		}else if(key.equals(KEY_PREF_LIGHTCONTROL)){
	        Intent intent = new Intent(this, LightControlActivity.class);
			addShortcut(this, intent, R.string.setting_lightcontrol, R.drawable.light_on);
			Toast.makeText(this, R.string.lockscreen_added_toast, Toast.LENGTH_LONG).show();
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
		klilog.i("dialog show");
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
    
	public static void addShortcut(Context context, Intent intent, int name, int icon) {
		 
        String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        // 快捷方式要启动的包

        // 设置快捷方式的参数
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        // 设置名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
                        .getString(name)); // 设置启动 Intent
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 设置图标
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(context,
                        		icon));
        // 只创建一次快捷方式
        shortcutIntent.putExtra("duplicate", false);
        // 创建
        context.sendBroadcast(shortcutIntent);

	}

}