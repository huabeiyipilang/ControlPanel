package cn.kli.controlpanel.module.quickpanel;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.module.floatpanel.FloatPanelLauncher;
import cn.kli.utils.klilog;

public class QuickPanelService extends Service {

	public static final String SERVICE_CMD = "cmd";
	
	public static final int MSG_SHOW_QUICK_PANEL = 1;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_SHOW_QUICK_PANEL:
			    QuickPanelManager.getInstance(QuickPanelService.this).showQuickPanel();
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
        Intent intent_show = new Intent(this, FloatPanelLauncher.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent_show,0);
        Notification notif = new Notification();
        notif.icon = R.drawable.ic_logo;
        notif.tickerText = getResources().getText(R.string.app_name);
        notif.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
        notif.setLatestEventInfo(this, 
                getResources().getText(R.string.app_name), 
                getResources().getText(R.string.notification_summary), 
                contentIntent);
        startForeground(1001, notif);
        klilog.info("startforground");
        mHandler.sendEmptyMessage(MSG_SHOW_QUICK_PANEL);
		return START_STICKY;
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
	
	
}
