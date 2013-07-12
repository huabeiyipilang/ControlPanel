package cn.kli.controlpanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DisplayControl extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent intent = new Intent();
        intent.putExtra("action", "android.settings.DISPLAY_SETTINGS");
        startActivity(intent);
        finish();
	}

}
