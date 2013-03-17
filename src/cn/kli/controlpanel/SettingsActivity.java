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
			addShortcut(this);
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
    
	public static void addShortcut(Context context) {
		StatService.onEvent(context, Baidu.EVENT_LOCK_SCREEN, Baidu.ADD_SHORTCUTS);
		 
        String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        // ��ݷ�ʽҪ�����İ�
        Intent intent = new Intent(context, OneKeyLockScreen.class);

        // ���ÿ�ݷ�ʽ�Ĳ���
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        // ��������
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
                        .getString(R.string.setting_lockscreen)); // �������� Intent
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // ����ͼ��
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(context,
                                        R.drawable.lock));
        // ֻ����һ�ο�ݷ�ʽ
        shortcutIntent.putExtra("duplicate", false);
        // ����
        context.sendBroadcast(shortcutIntent);

	}

}