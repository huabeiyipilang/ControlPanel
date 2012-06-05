package cn.kli.controlpanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ControlPanel extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("onCreate()");
        startService(new Intent(this,MainService.class));
        finish();
    }

}
