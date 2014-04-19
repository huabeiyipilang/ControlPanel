package cn.kli.controlpanel.base;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import cn.kli.utils.klilog;

public class FloatView {
    private Context mContext;
	protected View mContentView;
	private WindowManager mWinManager;
	private WindowManager.LayoutParams mParams;
	private boolean isPanelShow = false;
	private boolean isLock = false;
	
	public FloatView(Context context){
	    mContext = context;
		mWinManager =  (WindowManager) getContext().getSystemService("window");
	}
	
	protected Context getContext() {
        return mContext;
    }

    public void setContentView(View view){
		mContentView = view;
//		this.addView(mContentView);
        onFloatPrepare();
	}
	
	protected int onInitDragView(){
		return 0;
	}
	
	private void onFloatPrepare(){
		initParams();
		initDragView();
	}
	
	protected View getContentView(){
		return mContentView;
	}
	
	public void lock(boolean enable){
		klilog.info("lock enable:" + enable);
		isLock = enable;
	}
	
	private void initDragView(){
		View dragView = mContentView.findViewById(onInitDragView());
		if(dragView == null){
			dragView = mContentView;
		}
		dragView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
			}
		});
		dragView.setOnTouchListener(new OnDragListener());
	}
	
	private class OnDragListener implements OnTouchListener{
		float startX;
		float startY;
		int originX, originY;
		float downTime;
		
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				startX = event.getRawX();
				startY = event.getRawY();
				originX = mParams.x;
				originY = mParams.y;
				downTime = System.currentTimeMillis();
				return false;
			case MotionEvent.ACTION_MOVE:
				updateLocation(event.getRawX() - startX,event.getRawY() - startY, originX, originY);
				break;
			case MotionEvent.ACTION_UP:
				float moveX = event.getRawX() - startX;
				float moveY = event.getRawY() - startY;
				if(Math.abs(moveX) < 15 && Math.abs(moveY) < 15 && System.currentTimeMillis() - downTime < 500){
					return false;
				}
				onActionUp(moveX, moveY, originX, originY);
				break;
			}
			return true;
		}
	}
	
	protected void onActionUp(float x, float y, int originX, int originY){
		if(isLock){
			return;
		}
		setLocation((int)x + originX, (int)y + originY);
	}
	
	public void setLocation(float x, float y){
		mParams.x = (int)x;
		mParams.y = (int)y;
		mWinManager.updateViewLayout(mContentView, mParams);
	}
	
	private void updateLocation(float x, float y, int originX, int originY){
		if(isLock){
			return;
		}
		mParams.x = (int)x + originX;
		mParams.y = (int)y + originY;
		mWinManager.updateViewLayout(mContentView, mParams);
	}
	
	private void initParams() {

		//init params
		mParams = new WindowManager.LayoutParams();
		mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//		mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mParams.format = 1;
		mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
						WindowManager.LayoutParams.FLAG_FULLSCREEN | 
						WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.gravity = Gravity.CENTER;
	}
	
	public int getPositionX(){
		return mParams.x;
	}

	public int getPositionY(){
		return mParams.y;
	}
	
	public void openPanel(){
		if(mWinManager != null && mContentView != null && mParams != null && !isShow()){
			try {
				mWinManager.addView(mContentView, mParams);
				isPanelShow = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closePanel(){
		if(mWinManager != null && mContentView != null && isShow()){
			try {
				mWinManager.removeView(mContentView);
				isPanelShow = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isShow(){
		return isPanelShow;
	}
	
	protected void onConfigurationChanged(Configuration newConfig) {
		
	}
}
