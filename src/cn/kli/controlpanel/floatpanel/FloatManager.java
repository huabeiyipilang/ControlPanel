package cn.kli.controlpanel.floatpanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.WindowManager;

public class FloatManager {
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
		if(enable){
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
}
