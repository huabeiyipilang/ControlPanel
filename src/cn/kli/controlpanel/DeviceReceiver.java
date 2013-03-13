package cn.kli.controlpanel;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceReceiver extends DeviceAdminReceiver {

	private SettingPrefs mPrefs;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		mPrefs = SettingPrefs.getInstance(context);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		mPrefs.setDeviceAdminEnable(true);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onDisabled(context, intent);
		mPrefs.setDeviceAdminEnable(false);
	}

	
}
