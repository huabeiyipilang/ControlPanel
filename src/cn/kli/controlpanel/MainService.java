package cn.kli.controlpanel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import cn.kli.controlpanel.module.floatpanel.FloatPanelLauncher;
import cn.kli.controlpanel.module.floatpanel.SettingsActivity;
import cn.kli.utils.klilog;

import com.baidu.mobstat.StatService;

public class MainService extends Service{
	
	public static final String SERVICE_CMD = "cmd";
	public static final int CMD_BOOT_COMPLETED 		= 2;
	public static final int CMD_UPDATE_NOTIFICATION = 3;
	public static final int CMD_LIGHT_SWITCH 		= 4;
	
	private final static int NOTIFICATION_ID = 0;
	private final static String NOTIFICATION_TAG = "notification";
	
	//status
	private boolean isNotifShow = false;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case CMD_BOOT_COMPLETED:
				bootCompletedPrepare();
				break;
			case CMD_UPDATE_NOTIFICATION:
				updateNotification();
				break;
			case CMD_LIGHT_SWITCH:
				break;
			}
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		klilog.info("MainService onCreate(");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int cmd = intent.getIntExtra(SERVICE_CMD, 0);
		Message msg = mHandler.obtainMessage(cmd);
		Bundle bundle = new Bundle();
		bundle.putParcelable("intent", intent);
		msg.setData(bundle);
		msg.sendToTarget();
		return START_NOT_STICKY;
	}

    
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	

    private void updateNotification(){
    	SharedPreferences sprf = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean inNotifOn = sprf.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION, false);
    	if(isNotifShow == inNotifOn){
    		return;
    	}
    	isNotifShow = inNotifOn;
    	if(inNotifOn){
    		showNotification();
    		klilog.info("notification on");
    	}else{
    		cancelNotification();
    		klilog.info("notification off");
    	}
    }
    
    private void showNotification(){
    	/*
    	Intent intent = new Intent(this, Launcher.class);
    	intent.putExtra(Launcher.START_FROM, "Notification");*/
    	
    	Intent intent_show = new Intent(this, FloatPanelLauncher.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent_show,0); 
    	
    	/* Notification.Builder   Since: API Level 11;
    	Notification.Builder notifBuilder = new Notification.Builder(this);
    	notif.setSmallIcon(R.drawable.ic_launcher)
		.setTicker(getResources().getText(R.string.app_name))
		.getNotification(); */
    	Notification notif = new Notification();
    	notif.icon = R.drawable.ic_logo;
    	notif.tickerText = getResources().getText(R.string.app_name);
    	notif.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
    	notif.setLatestEventInfo(this, 
    			getResources().getText(R.string.app_name), 
    			getResources().getText(R.string.notification_summary), 
    			contentIntent);
    	
    	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	nm.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notif);
    	isNotifShow = true;
    }
    
    private void cancelNotification(){
    	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	nm.cancel(NOTIFICATION_TAG, NOTIFICATION_ID);
    	isNotifShow = false;
    }
    
    private void bootCompletedPrepare(){
    	updateNotification();
    }
	
}
