package cn.kli.controlpanel.floatpanel;

import cn.kli.utils.klilog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public abstract class FloatView {
	private Context mContext;
	private View mContentView;
	private WindowManager mWinManager;
	private WindowManager.LayoutParams mParams;
	private int screenWidth, screenHeight;
	private boolean isPanelShow = false;
	
	public FloatView(Context context, WindowManager winManager){
		mContext = context;
		mWinManager = winManager;
	}
	
	protected void onLayoutPrepare(int contentLayout, int dragView){
		
	}
	
	private void onFloatPrepare(){
		initParams();
		mContentView.setOnTouchListener(new OnTouchListener(){
			float startX;
			float startY;
			int originX, originY;
			View activeView;
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					//位移起点
					startX = event.getRawX();
					startY = event.getRawY();
					//FloatPanel初始坐标
					originX = mParams.x;
					originY = mParams.y;
					break;
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					updateLocation(event.getRawX() - startX,event.getRawY() - startY);
					break;
				}
				return true;
			}
			
			private void updateLocation(float x, float y){
				//新坐标 = 位移 + 初始坐标
				mParams.x = (int)x + originX;
				mParams.y = (int)y + originY;
				mWinManager.updateViewLayout(mContentView, mParams);
			}
			
		});
	}
	
	private void initParams() {
    	klilog.i("1initFloat() mFloatPanel.getHeight() = "+mContentView.getHeight());

		//init params
		mParams = new WindowManager.LayoutParams();
		mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mParams.format = 1;
		mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.gravity = Gravity.CENTER; // Gravity.TOP | Gravity.LEFT;
		
		klilog.i("screenWidth = "+screenWidth);
		klilog.i("screenHeight = "+screenHeight);
		klilog.i("mFloatPanel.getWidth() = "+mContentView.getWidth());
		klilog.i("mFloatPanel.getHeight() = "+mContentView.getHeight());
    	klilog.i("2initFloat() mFloatPanel.getHeight() = "+mContentView.getHeight());
	}
	
	public void openPanel(){
		if(mWinManager != null && mContentView != null && mParams != null){
			try {
				closePanel();
				mWinManager.addView(mContentView, mParams);
				isPanelShow = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closePanel(){
		if(mWinManager != null && mContentView != null){
			try {
				mWinManager.removeView(mContentView);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		isPanelShow = false;
	}
	
	public boolean isShow(){
		return isPanelShow;
	}
}
