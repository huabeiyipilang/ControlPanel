package cn.kli.controlpanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LightControlActivity extends Activity implements OnClickListener {
	
	private boolean isLightOn;
	private ImageButton mIbSwitch;
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
	
	
}
