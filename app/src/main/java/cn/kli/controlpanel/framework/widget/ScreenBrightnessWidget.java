package cn.kli.controlpanel.framework.widget;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.view.WindowManager;
import cn.kli.controlpanel.R;

public class ScreenBrightnessWidget extends AControlBar{
	private Context mContext;

	public ScreenBrightnessWidget(Context context) {
		super(context);
		init(context);
	}
	
	public ScreenBrightnessWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		mContext = context;
		int current = 255;
		try {
			current = android.provider.Settings.System.getInt(context.getContentResolver()
					, android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		int max = 255;
		setIcon(R.drawable.ic_settings_display);
		setBar(max, current);
		setDescription(R.string.description_screen_brightness);

	}
	
	@Override
	void updateValue(int value) {
		if(value == 0) return;
		android.provider.Settings.System.putInt(mContext.getContentResolver(), 
				android.provider.Settings.System.SCREEN_BRIGHTNESS, value);
	}

}
