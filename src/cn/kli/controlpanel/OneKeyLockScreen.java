package cn.kli.controlpanel;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

public class OneKeyLockScreen extends Activity {

	DevicePolicyManager devicePloicyManager;
	PowerManager powerManager;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    boolean enable = SettingPrefs.getInstance(this).getDeviceAdminEnable();
	    if(enable){
			devicePloicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
			powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
			devicePloicyManager.lockNow();
	    }else{
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			ComponentName componentName = new ComponentName(this, DeviceReceiver.class); 
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			startActivity(intent);
	    }
	}

}
