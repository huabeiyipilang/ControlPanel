package cn.kli.controlpanel.module.quickpanel;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.module.floatpanel.FloatPanelLauncher;
import cn.kli.controlpanel.settings.SettingsWindow;
import cn.kli.utils.Conversion;
import cn.kli.utils.klilog;

public class QuickPanelService extends Service {

	public static final String SERVICE_CMD = "cmd";
	
	public static final int MSG_SHOW_QUICK_PANEL = 1;
    public static final int MSG_UPDATE_NOTIFICATION = 2;
    
    private NetworkSpeedManager mNetworkSpeedManager;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_SHOW_QUICK_PANEL:
			    QuickPanelManager.getInstance(QuickPanelService.this).showQuickPanel();
			    break;
			case MSG_UPDATE_NOTIFICATION:
			    updateNotification();
			    break;
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    updateNotification();
        mHandler.sendEmptyMessage(MSG_SHOW_QUICK_PANEL);
		return START_STICKY;
	}
	
	private void updateNotification(){
	    boolean networkSpeed = Prefs.getPrefs(this).getBoolean(SettingsWindow.SETTING_NOTIFI_NETWORK_SPEED, false);
//        Intent intent_show = new Intent(this, FloatPanelLauncher.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent_show,0);
        Notification notif = new Notification();
        if(networkSpeed){
            if(mNetworkSpeedManager == null){
                mNetworkSpeedManager = new NetworkSpeedManager();
            }
            notif.icon = mNetworkSpeedManager.getIcon();
            notif.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
            notif.largeIcon = Conversion.drawable2Bitmap(getResources().getDrawable(R.drawable.ic_logo));
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_main);
            contentView.setTextViewText(R.id.tv_title, getString(R.string.app_name));
            notif.contentView = contentView;
            startForeground(1001, notif);
            mHandler.removeMessages(MSG_UPDATE_NOTIFICATION);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_NOTIFICATION, getNotificationUpdateDuring());
        }else{
            if(mNetworkSpeedManager != null){
                mNetworkSpeedManager = null;
            }
            notif.icon = R.drawable.ic_logo;
            notif.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
            notif.setLatestEventInfo(this, 
                    getResources().getText(R.string.app_name), 
                    getResources().getText(R.string.notification_summary), 
                    null);
            startForeground(1001, notif);
        }
	}
	
	private long getNotificationUpdateDuring(){
	    return 3000;
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
	
	
}
