package cn.kli.controlpanel.floatpanel;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.ThemeSetting;
import cn.kli.controlwidgets.IWidget;
import cn.kli.controlwidgets.WidgetFactory;
import cn.kli.utils.klilog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class FloatPanel implements OnClickListener {
	private Context mContext;
	private View mFloatPanel;
	private WindowManager mWinManager;
	private WindowManager.LayoutParams mParams;
	private int screenWidth, screenHeight;
	private boolean isPanelShow = false;
	
	public FloatPanel(Context context, WindowManager winManager){
		mContext = context;
		mWinManager = winManager;
		initFloatPanel();
		loadWidgets();
		loadTheme();
		initFloat();
	}
	
	public boolean isShow(){
		return isPanelShow;
	}
	
	private void initFloatPanel(){
    	mFloatPanel = LayoutInflater.from(mContext).inflate(R.layout.control_panel, null);
	}

    private void loadWidgets(){
    	int[] wigetList = {1,2,3};
    	LinearLayout container = (LinearLayout)(mFloatPanel.findViewById(R.id.container));
    	container.removeAllViews();
    	IWidget widget;
    	for(int i: wigetList){
    		widget = WidgetFactory.createControlbar(mContext, i);
    		if(widget != null){
        		container.addView((View) widget);
    		}else{
    			
    		}
    	}
    	klilog.i("loadWidgets() mFloatPanel.getHeight() = "+mFloatPanel.getHeight());
    }
    
    
    private void loadTheme(){
    	int colorHeader,colorContainer;
		SharedPreferences share = mContext.getSharedPreferences(ThemeSetting.SETTING_PREFERENCES, Context.MODE_PRIVATE);
		if(share == null){
			colorHeader = mContext.getResources().getColor(R.color.translucent_background_dark);
			colorContainer = mContext.getResources().getColor(R.color.translucent_background);
		}else{
			colorHeader = share.getInt(ThemeSetting.THEME_HEADER_COLOR, 
					mContext.getResources().getColor(R.color.translucent_background_dark));
			colorContainer = share.getInt(ThemeSetting.THEME_CONTAINER_COLOR, 
					mContext.getResources().getColor(R.color.translucent_background));
		}
		View header = mFloatPanel.findViewById(R.id.header);
		header.setOnTouchListener(new OnTouchListener(){
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
				mWinManager.updateViewLayout(mFloatPanel, mParams);
			}
			
		});
		if(header != null) header.setBackgroundColor(colorHeader);
		View container = mFloatPanel.findViewById(R.id.container);
		if(container != null) container.setBackgroundColor(colorContainer);
//		View tags = mFloatPanel.findViewById(R.id.tags);
//		if(tags != null) tags.setBackgroundColor(colorHeader);
    }
    
	private void initFloat() {
    	klilog.i("1initFloat() mFloatPanel.getHeight() = "+mFloatPanel.getHeight());
		// get screen size;
//		DisplayMetrics dm = new DisplayMetrics();
//		mWinManager.getDefaultDisplay().getMetrics(dm);
//		screenWidth = dm.widthPixels;
//		screenHeight = dm.heightPixels;

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
		klilog.i("mFloatPanel.getWidth() = "+mFloatPanel.getWidth());
		klilog.i("mFloatPanel.getHeight() = "+mFloatPanel.getHeight());
//		mParams.x = (screenWidth - mFloatPanel.getWidth())/2;
//		mParams.y = (screenHeight - mFloatPanel.getHeight())/2;
		
    	klilog.i("2initFloat() mFloatPanel.getHeight() = "+mFloatPanel.getHeight());
	}

	
	public void openPanel(){
		if(mWinManager != null && mFloatPanel != null && mParams != null){
			try {
				closePanel();
				mWinManager.addView(mFloatPanel, mParams);
				isPanelShow = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closePanel(){
		if(mWinManager != null && mFloatPanel != null){
			try {
				mWinManager.removeView(mFloatPanel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		isPanelShow = false;
	}

	@Override
	public void onClick(View arg0) {
		
	}
}
