package cn.kli.controlpanel.modules;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.kli.controlpanel.Baidu;
import cn.kli.controlpanel.R;

import com.baidu.mobstat.StatService;

public class OneKeyLockScreen extends Activity {

	DevicePolicyManager devicePloicyManager;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		try {
			devicePloicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
			devicePloicyManager.lockNow();
			StatService.onEvent(this, Baidu.EVENT_LOCK_SCREEN, Baidu.LOCK_SCREEN);
		} catch (Exception e) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			ComponentName componentName = new ComponentName(this, DeviceReceiver.class); 
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			startActivity(intent);
			Toast.makeText(this, R.string.get_device_admin_toast, Toast.LENGTH_LONG).show();
		}
		finish();
	}

}
