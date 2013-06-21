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
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
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
		mScreenWidth = 540; //TODO: 需要获取屏幕宽度
		mStrategy = new FreeMemory();
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
		if(isFirstOpen){
			//设置初始位置
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
	
	private class FreeMemory implements Strategy{
		
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
	
	private interface Strategy{
		String getDisplay();
	}
}
