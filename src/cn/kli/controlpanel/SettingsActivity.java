package cn.kli.controlpanel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;

public class SettingsActivity extends PreferenceActivity implements
	OnSharedPreferenceChangeListener,OnPreferenceClickListener {
	
	private final static String KEY_PREF_NOTIFICATION = "key_notification";
	private final static String KEY_PREF_THEME = "key_theme";
	
	private final static int ID_NOTIFICATION = 0;
	
	private boolean mIsNotifShow;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.getDefaultSharedPreferences(this)
        	.registerOnSharedPreferenceChangeListener(this);
        findPreference(KEY_PREF_THEME).setOnPreferenceClickListener(this);
        updateNotification();
    }

	public boolean onPreferenceClick(Preference pref) {
		String key = pref.getKey();
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

    private void updateNotification(){
    	SharedPreferences sprf = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean inNotifOn = sprf.getBoolean(KEY_PREF_NOTIFICATION, false);
    	if(mIsNotifShow == inNotifOn){
    		return;
    	}
    	mIsNotifShow = inNotifOn;
    	if(inNotifOn){
    		showNotification();
    		KLog.i("notification on");
    	}else{
    		cancelNotification();
    		KLog.i("notification off");
    	}
    }
    
    private void showNotification(){
    	Intent intent = new Intent(this, ControlPanel.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,0); 

//    	Notification.Builder notifBuilder = new Notification.Builder(this);
//    	notif.setSmallIcon(R.drawable.ic_launcher)
//		.setTicker(getResources().getText(R.string.app_name))
//		.getNotification();
    	Notification notif = new Notification();
    	notif.icon = R.drawable.ic_launcher;
    	notif.tickerText = getResources().getText(R.string.app_name);
    	notif.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
    	notif.setLatestEventInfo(this, 
    			getResources().getText(R.string.app_name), 
    			getResources().getText(R.string.notification_summary), 
    			contentIntent);
    	
    	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	nm.notify(KEY_PREF_NOTIFICATION, ID_NOTIFICATION, notif);
    }
    
    private void cancelNotification(){
    	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	nm.cancel(KEY_PREF_NOTIFICATION, ID_NOTIFICATION);
    }

}