package cn.kli.controlpanel.framework;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import cn.kli.controlpanel.controlwindow.ControlPanelWindow;
import cn.kli.controlpanel.utils.FloatWindowManager;
import cn.kli.utils.klilog;

public class FloatView {
    private klilog log = new klilog(FloatView.class);

    private Context mContext;
	protected View mContentView;
	private WindowManager mWinManager;
	private WindowManager.LayoutParams mParams;
	private boolean isPanelShow = false;
	private boolean isLock = false;
    private View.OnLongClickListener mOnLongClickListener;
    private OnClickListener mOnClickListener;
    private Handler mHandler;
	
	public FloatView(Context context){
	    mContext = context;
		mWinManager =  (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mHandler = new Handler();
	}
	
	protected Context getContext() {
        return mContext;
    }

    public void setContentView(View view){
		mContentView = view;
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
        log.info("lock enable:" + enable);
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
        boolean dragMode;
        boolean action;

        LongClickCheck checkLongClick = new LongClickCheck();
		
		@Override
		public boolean onTouch(View view, MotionEvent event) {
            float moveX = event.getRawX() - startX;
            float moveY = event.getRawY() - startY;
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				startX = event.getRawX();
				startY = event.getRawY();
				originX = mParams.x;
				originY = mParams.y;
				downTime = System.currentTimeMillis();
                mHandler.postDelayed(checkLongClick, 500);
				return false;
			case MotionEvent.ACTION_MOVE:
                if(Math.abs(moveX) > 20 || Math.abs(moveY) > 20){
                    dragMode = true;
                }
				updateLocation(moveX, moveY, originX, originY);
                return false;
			case MotionEvent.ACTION_UP:
                log.i("action up");
				onActionUp(moveX, moveY, originX, originY);
                if(System.currentTimeMillis() - downTime < 500 && !dragMode && mOnClickListener != null){
                    mOnClickListener.onClick(getContentView());
                }
                mHandler.removeCallbacks(checkLongClick);
                dragMode = false;
				break;
			}
			return false;
		}

        private class LongClickCheck implements Runnable {

            @Override
            public void run() {
                if(!dragMode && mOnLongClickListener != null){
                    mOnLongClickListener.onLongClick(getContentView());
                }
            }
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
	
	public void show(){
		if(mWinManager != null && mContentView != null && mParams != null && !isShow()){
			try {
				mWinManager.addView(mContentView, mParams);
				isPanelShow = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void hide(){
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

    protected void setOnLongClickListener(View.OnLongClickListener longClickListener){
        mOnLongClickListener = longClickListener;
    }

    protected void setOnClickListener(OnClickListener clickListener){
        mOnClickListener = clickListener;
    }
}
