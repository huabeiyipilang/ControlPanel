package cn.kli.controlpanel.module.quickpanel;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import cn.kli.utils.klilog;

public class QuickPanelService extends Service {

	public static final String SERVICE_CMD = "cmd";
	
	private static final int MSG_SHOW_QUICK_PANEL = 1;
	
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
        mHandler.sendEmptyMessage(MSG_SHOW_QUICK_PANEL);
		return START_STICKY;
	}
	
}
