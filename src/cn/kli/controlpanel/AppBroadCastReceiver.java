package cn.kli.controlpanel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.text.TextUtils;

public class AppBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent_service = new Intent(context, MainService.class);
		String action = intent.getAction();
		if(TextUtils.isEmpty(action)){
			return;
		}else{
			KLog.i("broadcast received action is "+action);
		} 
		
		if(action.equals("cn.kli.controlpanel.action.SHOW_PANEL")){
			intent_service.putExtra(MainService.SERVICE_CMD, MainService.CMD_OPEN_CONTROL_PANEL);
			context.startService(intent_service);
		}else if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
			intent_service.putExtra(MainService.SERVICE_CMD, MainService.CMD_BOOT_COMPLETED);
			context.startService(intent_service);
		}
	}

}
