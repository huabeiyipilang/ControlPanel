package cn.kli.controlpanel.module.devicecontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AppControl extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent intent = new Intent("android.settings.APPLICATION_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
	}

}
