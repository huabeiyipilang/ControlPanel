package cn.kli.controlpanel;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Launcher extends Activity {
	public static final String START_FROM = "start from";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("onCreate()");
        String startFrom = getIntent().getStringExtra(START_FROM);
        KLog.i("launcher start from "+startFrom);
        StatService.onEvent(getApplicationContext(), Baidu.OPEN_PANEL, Baidu.OPEN_PANEL_FROM_LAUNCHER+":"+startFrom);
        startService(new Intent(this,MainService.class).putExtra(MainService.SERVICE_CMD, MainService.CMD_OPEN_CONTROL_PANEL));
        finish();
    }

}