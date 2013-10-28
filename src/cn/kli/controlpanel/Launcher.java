package cn.kli.controlpanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.kli.menuui.MenuUIActivity;

public class Launcher extends Activity {
	public static final String START_FROM = "start from";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MenuUIActivity.class));
        finish();
    }

}
