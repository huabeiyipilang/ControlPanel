package cn.kli.controlpanel.module.floatpanel;

import cn.kli.utils.UIUtils;
import cn.kli.utils.klilog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;

public class FloatPanelService extends Service {

	public static final String SERVICE_CMD = "cmd";
	
	public static final int CMD_SHOW_PANEL 	= 1;
	public static final int CMD_HIDE_PANEL 	= 2;
	public static final int CMD_SHOW_INDICATOR 	= 3;
	public static final int CMD_HIDE_INDICATOR 	= 4;
	private final static int CMD_START_CHECK_HOME = 5;
	private final static int CMD_STOP_CHECK_HOME = 6;
	
	private FloatManager mManager;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case CMD_SHOW_PANEL:
				getFloatManager().showPanel();
				break;
			case CMD_HIDE_PANEL:
				getFloatManager().hidePanel();
				break;
			case CMD_SHOW_INDICATOR:
				getFloatManager().showIndicator();
				break;
			case CMD_HIDE_INDICATOR:
				getFloatManager().hideIndicator();
				break;
			case CMD_START_CHECK_HOME:
				checkHome();
				this.sendEmptyMessageDelayed(CMD_START_CHECK_HOME, 1000);
				break;
			case CMD_STOP_CHECK_HOME:
				this.removeMessages(CMD_START_CHECK_HOME);
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
		FloatManager.getInstance(this).loadDefaultConfig();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			int cmd = intent.getIntExtra(SERVICE_CMD, CMD_SHOW_INDICATOR);
			klilog.i("start cmd:"+cmd);
			Message msg = mHandler.obtainMessage(cmd);
			msg.setData(intent.getExtras());
			msg.sendToTarget();
		}else{
			showIndicator(this);
		}
		return START_STICKY;
	}
	
	private void checkHome() {
		boolean isHome = UIUtils.isHome(this);
		if (isHome) {
			getFloatManager().showIndicator();
		} else {
			getFloatManager().hideIndicator();
		}
	}
	
	private FloatManager getFloatManager(){
		if(mManager == null){
			mManager = FloatManager.getInstance(this);
		}
		return mManager;
	}
	
	private static void startEmptyCmd(Context context, int cmd){
	    Intent intent = new Intent(context, FloatPanelService.class);
	    intent.putExtra(FloatPanelService.SERVICE_CMD, cmd);
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
