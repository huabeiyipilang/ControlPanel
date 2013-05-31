package cn.kli.controlpanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ControlActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		findViewById(R.id.ll_blank).setOnClickListener(this);
		findViewById(R.id.display).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.ll_blank:
			finish();
			break;
		case R.id.display:
			break;
		}
	}

	
}
