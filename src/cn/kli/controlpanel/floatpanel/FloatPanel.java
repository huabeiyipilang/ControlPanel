package cn.kli.controlpanel.floatpanel;

import cn.kli.controlpanel.ControlActivity;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.ThemeSetting;
import cn.kli.controlwidgets.IWidget;
import cn.kli.controlwidgets.WidgetFactory;
import cn.kli.utils.klilog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class FloatPanel extends FloatView implements OnClickListener {

	private Context mContext;

	public FloatPanel(Context context, WindowManager winManager) {
		super(context, winManager);
		mContext = context;
		loadWidgets();
		loadTheme();
		loadEvent();
	}

	@Override
	int onInflaterContentView() {
		return R.layout.control_panel;
	}
	
	protected int onInitDragView(){
		return R.id.header;
	}
	
	private void loadEvent(){
		getContentView().findViewById(R.id.setting).setOnClickListener(this);
		getContentView().findViewById(R.id.close).setOnClickListener(this);
		getContentView().findViewById(R.id.title).setOnClickListener(this);
	}
	
    private void loadWidgets(){
    	int[] wigetList = {1,2,3};
    	LinearLayout container = (LinearLayout)(getContentView().findViewById(R.id.container));
    	container.removeAllViews();
    	IWidget widget;
    	for(int i: wigetList){
    		widget = WidgetFactory.createControlbar(mContext, i);
    		if(widget != null){
        		container.addView((View) widget);
    		}else{
    			
    		}
    	}
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
		View header = getContentView().findViewById(R.id.header);
		if(header != null) header.setBackgroundColor(colorHeader);
		View container = getContentView().findViewById(R.id.container);
		if(container != null) container.setBackgroundColor(colorContainer);
    }
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title:
			closePanel();
			Intent titleIntent = new Intent(mContext, ControlActivity.class);
			titleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(titleIntent);
			break;
		case R.id.setting:
			closePanel();
			Intent settingIntent = new Intent(mContext, SettingsActivity.class);
			settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(settingIntent);
			break;
		case R.id.close:
			closePanel();
		}
	}
}
