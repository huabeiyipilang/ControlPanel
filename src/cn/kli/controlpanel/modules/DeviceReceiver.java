package cn.kli.controlpanel.modules;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import cn.kli.controlpanel.Prefs;

public class DeviceReceiver extends DeviceAdminReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		Prefs prefs = Prefs.getInstance();
		prefs.setDeviceAdminEnable(true);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onDisabled(context, intent);
		Prefs prefs = Prefs.getInstance();
		prefs.setDeviceAdminEnable(false);
	}

}
