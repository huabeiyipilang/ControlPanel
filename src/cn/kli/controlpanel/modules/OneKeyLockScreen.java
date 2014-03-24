package cn.kli.controlpanel.modules;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.os.Bundle;
import android.widget.Toast;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.utils.LockScreenUtils;

public class OneKeyLockScreen extends Activity {

	DevicePolicyManager devicePloicyManager;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    boolean res = LockScreenUtils.getInstance(this).lockScreen();
	    if(!res){
            Toast.makeText(this, R.string.get_device_admin_toast, Toast.LENGTH_LONG).show();
	    }
		finish();
	}

}
