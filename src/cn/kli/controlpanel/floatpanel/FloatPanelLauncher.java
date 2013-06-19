package cn.kli.controlpanel.floatpanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FloatPanelLauncher extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent intent = new Intent(this,FloatPanelService.class);
//	    intent.putExtra(FloatPanelService.SERVICE_CMD, FloatPanelService.CMD_SHOW_PANEL);
	    intent.putExtra(FloatPanelService.SERVICE_CMD, FloatPanelService.CMD_SHOW_INDICATOR);
	    startService(intent);
	    finish();
	}

}
