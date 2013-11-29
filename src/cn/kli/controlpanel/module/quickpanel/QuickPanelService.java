package cn.kli.controlpanel.module.quickpanel;

import cn.kli.utils.klilog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class QuickPanelService extends Service {

	public static final String SERVICE_CMD = "cmd";
	
	public static final int CMD_SHOW_PANEL 	= 1;
	public static final int CMD_HIDE_PANEL 	= 2;
	public static final int CMD_SHOW_INDICATOR 	= 3;
	public static final int CMD_HIDE_INDICATOR 	= 4;
	private final static int CMD_START_CHECK_HOME = 5;
	private final static int CMD_STOP_CHECK_HOME = 6;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
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
		if(intent != null){
			int cmd = intent.getIntExtra(SERVICE_CMD, CMD_SHOW_INDICATOR);
			klilog.info("start cmd:"+cmd);
			Message msg = mHandler.obtainMessage(cmd);
			msg.setData(intent.getExtras());
			msg.sendToTarget();
		}else{
			showIndicator(this);
		}
		return START_STICKY;
	}
	
	private static void startEmptyCmd(Context context, int cmd){
	    Intent intent = new Intent(context, QuickPanelService.class);
	    intent.putExtra(QuickPanelService.SERVICE_CMD, cmd);
	    context.startService(intent);
	}
	
	public static void showPanel(Context context){
		startEmptyCmd(context, CMD_SHOW_PANEL);
	}
	
	public static void showIndicator(Context context){
		startEmptyCmd(context, CMD_SHOW_INDICATOR);
	}
	
	public static void startLauncherCheck(Context context){
		startEmptyCmd(context, CMD_START_CHECK_HOME);
	}

	public static void stopLauncherCheck(Context context){
		startEmptyCmd(context, CMD_STOP_CHECK_HOME);
	}
}
