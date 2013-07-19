package cn.kli.controlpanel.modules;

import cn.kli.controlpanel.Prefs;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceReceiver extends DeviceAdminReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		Prefs prefs = Prefs.getInstance(context);
		prefs.setDeviceAdminEnable(true);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onDisabled(context, intent);
		Prefs prefs = Prefs.getInstance(context);
		prefs.setDeviceAdminEnable(false);
	}

}
