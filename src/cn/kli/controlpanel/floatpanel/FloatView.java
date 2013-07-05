package cn.kli.controlpanel.floatpanel;

import cn.kli.utils.klilog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public abstract class FloatView {
	protected Context mContext;
	protected View mContentView;
	private WindowManager mWinManager;
	private WindowManager.LayoutParams mParams;
	private boolean isPanelShow = false;
	
	public FloatView(Context context, WindowManager winManager){
		mContext = context;
		mWinManager = winManager;
		mContentView = LayoutInflater.from(mContext).inflate(onInflaterContentView(), null);
		onFloatPrepare();
	}
	
	abstract int onInflaterContentView();
	
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
	
	private void initDragView(){
		View dragView = mContentView.findViewById(onInitDragView());
		if(dragView == null){
			dragView = mContentView;
		}
		//�˴����ÿյ�OnClickListener����Ϊ�����ѵ�ACTION_DOWN�¼���
		//����OnDragListener��ACTION_DOWN����false�Ļ���ACTION_MOVE��ACTION_UP
		//�Ͳ��ᱻ��������������޷��϶���
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
				//λ�����
				startX = event.getRawX();
				startY = event.getRawY();
				//FloatPanel��ʼ����
				originX = mParams.x;
				originY = mParams.y;
				downTime = System.currentTimeMillis();
				//�˴�����false�����ACTION_UP����false�Ļ����Ϳ��Դ���onClick
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
		setLocation((int)x + originX, (int)y + originY);
	}
	
	protected void setLocation(float x, float y){
		mParams.x = (int)x;
		mParams.y = (int)y;
		mWinManager.updateViewLayout(mContentView, mParams);
	}
	
	private void updateLocation(float x, float y, int originX, int originY){
		//������ = λ�� + ��ʼ����
		mParams.x = (int)x + originX;
		mParams.y = (int)y + originY;
		mWinManager.updateViewLayout(mContentView, mParams);
	}
	
	private void initParams() {
    	klilog.i("1initFloat() mFloatPanel.getHeight() = "+mContentView.getHeight());

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
		
		klilog.i("mFloatPanel.getWidth() = "+mContentView.getWidth());
		klilog.i("mFloatPanel.getHeight() = "+mContentView.getHeight());
    	klilog.i("2initFloat() mFloatPanel.getHeight() = "+mContentView.getHeight());
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
