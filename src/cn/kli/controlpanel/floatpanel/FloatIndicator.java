package cn.kli.controlpanel.floatpanel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.utils.DeviceUtils;
import cn.kli.utils.UIUtils;
import cn.kli.utils.klilog;

public class FloatIndicator extends FloatView{

	private final static int FRESH_DURING = 3000;
	
	private final static int MSG_FRESH = 1;
	
	private int mScreenWidth;
	private boolean isFirstOpen = true;
	private Strategy mStrategy;
	private TextView mIndicatorDisplay;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_FRESH:
				mIndicatorDisplay.setText(mStrategy.getDisplay());
				this.sendEmptyMessageDelayed(MSG_FRESH, FRESH_DURING);
				break;
			}
		}
		
	};
	
	public FloatIndicator(Context context, WindowManager winManager) {
		super(context, winManager);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		String type = pref.getString(SettingsActivity.KEY_PREF_INDICATOR_TYPES, null);
		setStrategy(type);
		mIndicatorDisplay = (TextView)mContentView.findViewById(R.id.tv_indicator);
		mContentView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				closePanel();
				FloatPanelService.showPanel(mContext);
			}
			
		});
	}

	@Override
	int onInflaterContentView() {
		return R.layout.float_indicator;
	}

	@Override
	public void openPanel() {
		super.openPanel();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		mScreenWidth = pref.getInt(Prefs.PREF_SCREEN_WIDTH, 0);
		if(isFirstOpen){
			//ÉèÖÃ³õÊ¼Î»ÖÃ
			setLocation(mScreenWidth / 2, 0);
			isFirstOpen = false;
		}
		mHandler.sendEmptyMessage(MSG_FRESH);
	}
	
	@Override
	public void closePanel() {
		super.closePanel();
		mHandler.removeMessages(MSG_FRESH);
	}

	@Override
	protected void onActionUp(float x, float y, int originX, int originY) {
		x = x + originX > 0 ? mScreenWidth / 2 : - mScreenWidth / 2;
		originX = 0;
		super.onActionUp(x, y, originX, originY);
	}

	public void setStrategy(String value){
		klilog.i("switch indicator type:"+value);
		if("memoryused".equals(value)){
			mStrategy = new FreeMemoryStrategy();
		}else if("networkspeed".equals(value)){
			mStrategy = new NetSpeedStrategy();
		}else if("text".equals(value)){
			
		}else{
			mStrategy = new FreeMemoryStrategy();
		}
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
			double availMem = DeviceUtils.getAvailMemory(mContext);
			double totalMem = DeviceUtils.getTotalMemory();
			DecimalFormat df = new DecimalFormat("0");
			String display = df.format((1 - availMem / totalMem)*100);
			display += "%";
			return display;
		}
		
	}
	
}
