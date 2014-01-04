package cn.kli.controlpanel.module.indicator;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.FloatView;
import cn.kli.controlpanel.module.floatpanel.FloatPanelService;
import cn.kli.controlpanel.module.floatpanel.SettingsActivity;
import cn.kli.utils.DeviceUtils;
import cn.kli.utils.UIUtils;
import cn.kli.utils.klilog;

public class FloatIndicator extends FloatView{

	private final static int FRESH_DURING = 3000;
	
	private final static int MSG_FRESH = 1;
	
	private int mScreenWidth;
	private Strategy mStrategy;
	private TextView mIndicatorDisplay;
	private SharedPreferences mPref;
	private boolean mIsStatusbarMode = false;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_FRESH:
				if(mIndicatorDisplay != null && mStrategy != null){
					mIndicatorDisplay.setText(mStrategy.getDisplay());
				}
				this.sendEmptyMessageDelayed(MSG_FRESH, FRESH_DURING);
				break;
			}
		}
		
	};
	
	public FloatIndicator(Context context) {
		super(context);
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.float_indicator, null);
		this.setContentView(contentView);
		mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
		
		//init type
		String type = mPref.getString(SettingsActivity.KEY_PREF_INDICATOR_TYPES, null);
		setStrategy(type);
		
		//init view
		mIndicatorDisplay = (TextView)mContentView.findViewById(R.id.tv_indicator);
		
		//init statusbar mode
		mIsStatusbarMode = mPref.getBoolean(SettingsActivity.KEY_PREF_INDICATOR_STATUSBAR_MODE, false);
		setStatusbarMode(mIsStatusbarMode);
		mContentView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(!mIsStatusbarMode){
					closePanel();
				}
				FloatPanelService.showPanel(getContext());
			}
			
		});
		
		//init lock
		boolean lock = mPref.getBoolean(SettingsActivity.KEY_PREF_INDICATOR_LOCK, false);
		lock(lock);
	}
	
	public void setStatusbarMode(boolean enable){
		mIsStatusbarMode = enable;
		if(mIsStatusbarMode){
			UIUtils.isOrentationPortrait(getContext());
			int statusbarHeight = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_STATUSBAR_HEIGHT, 0);
			klilog.info("statusbarHeight = "+statusbarHeight);
			setLocation(0, -getScreenHeight()/2 + statusbarHeight/2);
			setBgColor(Color.TRANSPARENT);
		}else{
			setBgColor(getContext().getResources().getColor(R.color.translucent_background));
		}
	}
	
	private void setBgColor(int color){
		View view = mContentView.findViewById(R.id.ll_indicator_container);
		view.setBackgroundColor(color);
	}

	@Override
	public void openPanel() {
		super.openPanel();
		if(mIsStatusbarMode){
			setStatusbarMode(mIsStatusbarMode);
		}else{
			mScreenWidth = getScreenWidth();
			int x = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_INDICATOR_X, -1);
			int y = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_INDICATOR_Y, -1);
			if(x == -1 && y == -1){
				//���ó�ʼλ��
				setLocation(mScreenWidth / 2, 0);
			}else{
				setLocation(x, y);
			}
		}
		mHandler.removeMessages(MSG_FRESH);
		mHandler.sendEmptyMessage(MSG_FRESH);
	}
	
	@Override
	public void closePanel() {
		super.closePanel();
		mHandler.removeMessages(MSG_FRESH);
	}
	
	private int getScreenWidth(){
		int width = 0;
		Configuration config = getContext().getResources().getConfiguration();
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
			width = mPref.getInt(Prefs.PREF_SCREEN_WIDTH, 0);
		}else if(config.orientation ==Configuration.ORIENTATION_LANDSCAPE){
			width = mPref.getInt(Prefs.PREF_SCREEN_HEIGHT, 0);
		}
		return width;
	}	
	
	private int getScreenHeight(){
		int height = 0;
		Configuration config = getContext().getResources().getConfiguration();
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
			height = mPref.getInt(Prefs.PREF_SCREEN_HEIGHT, 0);
		}else if(config.orientation ==Configuration.ORIENTATION_LANDSCAPE){
			height = mPref.getInt(Prefs.PREF_SCREEN_WIDTH, 0);
		}
		return height;
	}

	@Override
	public void setLocation(float x, float y) {
		if(!isShow()){
			return;
		}
		if(mPref.getBoolean(SettingsActivity.KEY_PREF_INDICATOR_AUTO_EDGE, true)){
			x = x > 0 ? mScreenWidth / 2 : - mScreenWidth / 2;
		}
		super.setLocation(x, y);
		Editor editor = Prefs.getPrefs(getContext()).edit();
		editor.putInt(Prefs.PREF_INDICATOR_X, (int) x);
		editor.putInt(Prefs.PREF_INDICATOR_Y, (int) y);
		editor.commit();
	}

	public void setStrategy(String value){
		klilog.info("switch indicator type:"+value);
		if("memoryused".equals(value)){
			mStrategy = new FreeMemoryStrategy();
		}else if("networkspeed".equals(value)){
			mStrategy = new NetSpeedStrategy();
		}else if("text".equals(value)){
			
		}else{
			mStrategy = new FreeMemoryStrategy();
		}
	}
	
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(mIsStatusbarMode){
			setStatusbarMode(mIsStatusbarMode);
			return;
		}
		mScreenWidth = getScreenWidth();
		int x = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_INDICATOR_X, -1);
		int y = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_INDICATOR_Y, -1);
		int screenWidth = 0, screenHeight = 0;
		if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
			screenWidth = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_SCREEN_WIDTH, -1);
			screenHeight = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_SCREEN_HEIGHT, -1);
        }else if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
			screenHeight= Prefs.getPrefs(getContext()).getInt(Prefs.PREF_SCREEN_WIDTH, -1);
			screenWidth = Prefs.getPrefs(getContext()).getInt(Prefs.PREF_SCREEN_HEIGHT, -1);
        }
		setLocation(getMax(x, screenWidth/2), getMax(y, screenHeight/2));
	}

	private int getMax(int value, int max){
		if(value > max){
			value = max;
		}else if(value < -max){
			value = -max;
		}
		return value;
	}

	private interface Strategy{
		String getDisplay();
	}
	
	private class NetSpeedStrategy implements Strategy{
		private long oldTotalBytes = 0L;
		private long oldTime;
		@Override
		public String getDisplay() {
			long newTotalBytes = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
			long newTime = System.currentTimeMillis();
			float speed = 0;
			try {
				speed = (newTotalBytes - oldTotalBytes) * 1000/(newTime - oldTime); // b/s
			} catch (Exception e) {
				
			}
			oldTotalBytes = newTotalBytes;
			oldTime = newTime;
			return formatSpeed(speed);
		}
		
		private String formatSpeed(float speed){
			String res = "";
			if(speed < 1000){	// b/s
				res = "B/s";
			}else if(speed < 1000000){	// kb/s
				speed = speed/1000;
				res = "K/s";
			}else if(speed < 1000000000){	// mb/s
				speed = speed/1000000;
				res = "M/s";
			}
			DecimalFormat df = new DecimalFormat("0");
			res = df.format(speed) + res;
			return res;
		}
	}
	
	private class FreeMemoryStrategy implements Strategy{
		
		@Override
		public String getDisplay() {
			double availMem = DeviceUtils.getAvailMemory(getContext());
			double totalMem = DeviceUtils.getTotalMemory();
			DecimalFormat df = new DecimalFormat("0");
			String display = df.format((1 - availMem / totalMem)*100);
			display += "%";
			return display;
		}
		
	}
	
}