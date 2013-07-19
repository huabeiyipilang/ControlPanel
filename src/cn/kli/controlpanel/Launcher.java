package cn.kli.controlpanel;

import cn.kli.controlpanel.launcher.ControlActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Launcher extends Activity {
	public static final String START_FROM = "start from";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, ControlActivity.class));
        finish();
    }

}
