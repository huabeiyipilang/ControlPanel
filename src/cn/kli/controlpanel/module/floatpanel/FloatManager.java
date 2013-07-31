package cn.kli.controlpanel.module.floatpanel;

import cn.kli.controlpanel.App;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.App.ConfigurationListener;
import cn.kli.controlpanel.R;
import cn.kli.utils.klilog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.WindowManager;


public class FloatManager implements ConfigurationListener{
	private static FloatManager sInstance;
	
	private Context mContext;
	private FloatPanel mFloatPanel;
	private FloatIndicator mIndicator;
	private WindowManager mWinManager;
	
	public static FloatManager getInstance(Context context){
		if(sInstance == null){
			sInstance = new FloatManager(context);
		}
		return sInstance;
	}
	
	private FloatManager(Context context){
		mContext = context;
		mWinManager = (WindowManager) mContext.getSystemService("window");
		mIndicator = new FloatIndicator(context, mWinManager);
		mFloatPanel = new FloatPanel(context, mWinManager);
		App.registerConfigListener(this);
	}
	
	public void showPanel(){
		mFloatPanel.openPanel();
	}
	
	public void hidePanel(){
		mFloatPanel.closePanel();
	}
	
	public void showIndicator(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		boolean enable = pref.getBoolean(SettingsActivity.KEY_PREF_INDICATOR_SWITCH, false);
		if(enable && !mFloatPanel.isShow()){
			mIndicator.openPanel();
		}
	}
	
	public void hideIndicator(){
		mIndicator.closePanel();
	}
	
	public void setIndicatorType(String type){
		mIndicator.setStrategy(type);
	}
	
	public FloatIndicator getIndicator(){
		return mIndicator;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		mIndicator.onConfigurationChanged(newConfig);
	}

	public void updateIndicatorLocation(float x, float y) {
		mIndicator.setLocation(x, y);
	}
	
	public void loadDefaultConfig(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		String type = pref.getString(SettingsActivity.KEY_PREF_INDICATOR_TYPES, null);
		if(TextUtils.isEmpty(type)){
			Editor editor = pref.edit();
			editor.putBoolean(SettingsActivity.KEY_PREF_INDICATOR_SWITCH,
					mContext.getResources().getBoolean(R.bool.def_indicator_enable));
			editor.putBoolean(SettingsActivity.KEY_PREF_INDICATOR_LAUNCHER_SWITCH,
					mContext.getResources().getBoolean(R.bool.def_indicator_launcher_only));
			editor.putBoolean(SettingsActivity.KEY_PREF_INDICATOR_AUTO_EDGE,
					mContext.getResources().getBoolean(R.bool.def_indicator_auto_edge));
			editor.commit();
		}
	}
}
