package cn.kli.controlpanel.module.floatpanel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import cn.kli.controlpanel.Prefs;

public class FloatPanelLauncher extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getInt(Prefs.PREF_SCREEN_WIDTH, 0) == 0){
			DisplayMetrics metrics = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int h,w;
			if(metrics.heightPixels > metrics.widthPixels){
				w = metrics.widthPixels;
				h = metrics.heightPixels;
			}else{
				h = metrics.widthPixels;
				w = metrics.heightPixels;
			}
			Editor editor = pref.edit();
			editor.putInt(Prefs.PREF_SCREEN_WIDTH, w);
			editor.putInt(Prefs.PREF_SCREEN_HEIGHT, h);
			editor.commit();
		}
	    Intent intent = new Intent(this,FloatPanelService.class);
	    intent.putExtra(FloatPanelService.SERVICE_CMD, FloatPanelService.CMD_SHOW_PANEL);
	    startService(intent);
	    finish();
	}

}
