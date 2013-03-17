package cn.kli.controlpanel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.kli.controlwidgets.IWidget;
import cn.kli.controlwidgets.WidgetFactory;

import com.baidu.mobstat.StatService;

public class MainService extends Service implements OnClickListener {
	
	public static final String SERVICE_CMD = "cmd";
	public static final int CMD_OPEN_CONTROL_PANEL = 1;
	public static final int CMD_BOOT_COMPLETED = 2;
	public static final int CMD_UPDATE_NOTIFICATION = 3;
	
	private final static int NOTIFICATION_ID = 0;
	private final static String NOTIFICATION_TAG = "notification";
	
	private WindowManager mWinManager;
	private View mFloatPanel;
	private WindowManager.LayoutParams mParams;
	private int screenWidth, screenHeight;
	
	//status
	private boolean isPanelShow = false;
	private boolean isNotifShow = false;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case CMD_OPEN_CONTROL_PANEL:
				openControlPanel();
				break;
			case CMD_BOOT_COMPLETED:
				bootCompletedPrepare();
				break;
			case CMD_UPDATE_NOTIFICATION:
				updateNotification();
				break;
			}
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		klilog.i("MainService onCreate(");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int cmd = intent.getIntExtra(SERVICE_CMD, 0);
		Message msg = mHandler.obtainMessage(cmd);
		Bundle bundle = new Bundle();
		bundle.putParcelable("intent", intent);
		msg.setData(bundle);
		msg.sendToTarget();
		return START_NOT_STICKY;
	}
	
	private void openControlPanel(){
		if(isPanelShow){
			return;
		}
		
		if(mFloatPanel == null){
			initFloatPanel();
		}
		
    	loadWidgets();
    	loadTheme();
		initFloat();
	}
	
	private void initFloatPanel(){
    	mFloatPanel = LayoutInflater.from(this).inflate(R.layout.control_panel, null);
		mFloatPanel.findViewById(R.id.setting).setOnClickListener(this);
		mFloatPanel.findViewById(R.id.close).setOnClickListener(this);
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
    	klilog.i("loadWidgets() mFloatPanel.getHeight() = "+mFloatPanel.getHeight());
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
		//init WindowManager
		mWinManager = (WindowManager) getSystemService("window");

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
		
		//reopen panel
		closePanel();
		
		openPanel();
    	klilog.i("2initFloat() mFloatPanel.getHeight() = "+mFloatPanel.getHeight());
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
				isPanelShow = true;
				
				StatService.onResume(this);
				StatService.onEvent(getApplicationContext(), "openPanel()", "openPanel()");
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
		isPanelShow = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	

    private void updateNotification(){
    	SharedPreferences sprf = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean inNotifOn = sprf.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION, false);
    	if(isNotifShow == inNotifOn){
    		return;
    	}
    	isNotifShow = inNotifOn;
    	if(inNotifOn){
    		showNotification();
    		klilog.i("notification on");
    	}else{
    		cancelNotification();
    		klilog.i("notification off");
    	}
    }
    
    private void showNotification(){
    	StatService.onEvent(this, Baidu.SETTINGS, "show notification");
    	/*
    	Intent intent = new Intent(this, Launcher.class);
    	intent.putExtra(Launcher.START_FROM, "Notification");*/
    	
    	Intent intent_show = new Intent();
    	intent_show.setAction("cn.kli.controlpanel.action.SHOW_PANEL");
    	PendingIntent contentIntent = PendingIntent.getBroadcast(this,0,intent_show,0); 
    	
    	/* Notification.Builder   Since: API Level 11;
    	Notification.Builder notifBuilder = new Notification.Builder(this);
    	notif.setSmallIcon(R.drawable.ic_launcher)
		.setTicker(getResources().getText(R.string.app_name))
		.getNotification(); */
    	Notification notif = new Notification();
    	notif.icon = R.drawable.ic_logo;
    	notif.tickerText = getResources().getText(R.string.app_name);
    	notif.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
    	notif.setLatestEventInfo(this, 
    			getResources().getText(R.string.app_name), 
    			getResources().getText(R.string.notification_summary), 
    			contentIntent);
    	
    	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	nm.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notif);
    	isNotifShow = true;
    }
    
    private void cancelNotification(){
    	StatService.onEvent(this, Baidu.SETTINGS, "cancel notification");
    	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	nm.cancel(NOTIFICATION_TAG, NOTIFICATION_ID);
    	isNotifShow = false;
    }
    
    private void bootCompletedPrepare(){
    	updateNotification();
    }
	
}
