package cn.kli.controlpanel.floatpanel;

import android.content.Context;
import android.view.WindowManager;

public class FloatManager {
	private Context mContext;
	private FloatPanel mFloatPanel;
	private WindowManager mWinManager;
	
	public FloatManager(Context context){
		mContext = context;
		mWinManager = (WindowManager) mContext.getSystemService("window");
		mFloatPanel = new FloatPanel(context, mWinManager);
	}
	
	public void showPanel(){
		mFloatPanel.openPanel();
	}
	
	public void hidePanel(){
		mFloatPanel.closePanel();
	}
}
