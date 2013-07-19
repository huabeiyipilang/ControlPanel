package cn.kli.controlpanel.modules;

import java.io.DataOutputStream;
import java.io.IOException;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.R.drawable;
import cn.kli.controlpanel.R.id;
import cn.kli.controlpanel.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LightControlActivity extends Activity implements OnClickListener {
	
	private boolean isLightOn;
	private ImageButton mIbSwitch;
	
	private boolean hasSu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.light_control_activity);
	    mIbSwitch = (ImageButton)findViewById(R.id.ib_light_switch);
	    mIbSwitch.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.ib_light_switch:
			switchLight();
//			homeKeyEvent();
			break;
		}
	}

	private void switchLight(){
		if(isLightOn){
			lightTurnOff();
		}else{
			lightTurnOn();
		}
		isLightOn = !isLightOn;
	}
	
	private void lightTurnOn(){
		//TODO:
		mIbSwitch.setImageResource(R.drawable.light_on);
	}
	
	private void lightTurnOff(){
		//TODO:
		mIbSwitch.setImageResource(R.drawable.light_off);
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(isLightOn){
			switchLight();
		}
	}
	
	private void homeKeyEvent(){
		new Thread(){

			@Override
			public void run() {
				super.run();
				if(hasSu){
					try {
						Runtime.getRuntime().exec("input keyevent 3 \n");
						Log.i("klilog", "send keyevent 3");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					rootCommand("input keyevent 3 \n");
					Log.i("klilog", "send keyevent 3 su");
				}
			}
			
		}.start();
	}
	
	private boolean rootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			hasSu = true;
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				// nothing
			}
		}

		Log.d("*** DEBUG ***", "Root SUC ");
		return true;
	}
	
}
