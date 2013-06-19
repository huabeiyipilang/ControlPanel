package cn.kli.controlpanel.floatpanel;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class FloatPanelService extends Service {

	public static final String SERVICE_CMD = "cmd";
	
	public static final int CMD_SHOW_PANEL 	= 1;
	public static final int CMD_HIDE_PANEL 	= 2;
	public static final int CMD_SHOW_INDICATOR 	= 3;
	public static final int CMD_HIDE_INDICATOR 	= 4;
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
			}
		}
	};
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int cmd = intent.getIntExtra(SERVICE_CMD, 0);
		Message msg = mHandler.obtainMessage(cmd);
		msg.setData(intent.getExtras());
		msg.sendToTarget();
		return START_NOT_STICKY;
	}
	
	private FloatManager getFloatManager(){
		if(mManager == null){
			mManager = new FloatManager(this);
		}
		return mManager;
	}
}
