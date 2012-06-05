package cn.kli.controlpanel;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import cn.kli.controlwidgets.IWidget;
import cn.kli.controlwidgets.WidgetFactory;

import com.baidu.mobstat.StatService;

public class MainService extends Service implements OnClickListener {
	private WindowManager mWinManager;
	private View mFloatPanel;
	private WindowManager.LayoutParams mParams;
	private int screenWidth, screenHeight;

	@Override
	public void onCreate() {
		super.onCreate();
		KLog.i("MainService onCreate(");
    	mFloatPanel = LayoutInflater.from(this).inflate(R.layout.float_panel, null);
		mFloatPanel.findViewById(R.id.setting).setOnClickListener(this);
		mFloatPanel.findViewById(R.id.close).setOnClickListener(this);
		
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
    	loadWidgets();
    	loadTheme();
		initFloat();
	}

	private void initFloat() {
		//init WindowManager
		mWinManager = (WindowManager) getSystemService("window");

		// get screen size;
		DisplayMetrics dm = new DisplayMetrics();
		mWinManager.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		//init params
		mParams = new WindowManager.LayoutParams();
		mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mParams.format = 1;
		mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.gravity = Gravity.TOP | Gravity.LEFT;
		
		KLog.i("screenWidth = "+screenWidth);
		KLog.i("screenHeight = "+screenHeight);
		KLog.i("mFloatPanel.getWidth() = "+mFloatPanel.getWidth());
		KLog.i("mFloatPanel.getHeight() = "+mFloatPanel.getHeight());
		mParams.x = (screenWidth - mFloatPanel.getWidth())/2;
		mParams.y = (screenHeight - mFloatPanel.getHeight())/2;
		
		//reopen panel
		closePanel();
		openPanel();
	}

    private void loadWidgets(){
    	int[] wigetList = {1,2,3};
    	LinearLayout container = (LinearLayout)(mFloatPanel.findViewById(R.id.container));
    	container.removeAllViews();
    	IWidget widget;
    	for(int i: wigetList){
    		widget = WidgetFactory.createControlbar(this, i);
    		if(widget != null){
        		container.addView((View) widget);
    		}else{
    			
    		}
    	}
    }
    
    private void loadTheme(){
    	int colorHeader,colorContainer;
		SharedPreferences share = getSharedPreferences(ThemeSetting.SETTING_PREFERENCES, MODE_PRIVATE);
		if(share == null){
			colorHeader = getResources().getColor(R.color.translucent_background_dark);
			colorContainer = getResources().getColor(R.color.translucent_background);
		}else{
			colorHeader = share.getInt(ThemeSetting.THEME_HEADER_COLOR, 
					getResources().getColor(R.color.translucent_background_dark));
			colorContainer = share.getInt(ThemeSetting.THEME_CONTAINER_COLOR, 
					getResources().getColor(R.color.translucent_background));
		}
		View header = mFloatPanel.findViewById(R.id.header);
		header.setOnTouchListener(new OnTouchListener(){
			float touchX;
			float touchY;
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				float x = event.getRawX();
				float y = event.getRawY() - 25;
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					touchX = event.getX();
					touchY = event.getY() + view.getHeight()/2;
					KLog.i("ACTION_DOWN: touchX = "+touchX+", touchY = "+touchY);
					KLog.i("ACTION_DOWN: 	  x = "+x+",      y = "+y);
					break;
				case MotionEvent.ACTION_MOVE:
					updateLocation(x - touchX,y - touchY);
					break;
				case MotionEvent.ACTION_UP:
					updateLocation(x - touchX,y - touchY);
					touchX = touchY = 0;
					break;
				}
				return true;
			}
			
			private void updateLocation(float x, float y){
				mParams.x = (int)x;
				mParams.y = (int)y;
				mWinManager.updateViewLayout(mFloatPanel, mParams);
			}
			
		});
		if(header != null) header.setBackgroundColor(colorHeader);
		View container = mFloatPanel.findViewById(R.id.container);
		if(container != null) container.setBackgroundColor(colorContainer);
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.setting:
			closePanel();
			Intent intent = new Intent(this,SettingsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.close:
			closePanel();
		}
	}
	
	private void openPanel(){
		if(mWinManager != null && mFloatPanel != null && mParams != null){
			try {
				mWinManager.addView(mFloatPanel, mParams);
				StatService.onResume(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closePanel(){
		if(mWinManager != null && mFloatPanel != null){
			try {
				mWinManager.removeView(mFloatPanel);
				StatService.onPause(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}