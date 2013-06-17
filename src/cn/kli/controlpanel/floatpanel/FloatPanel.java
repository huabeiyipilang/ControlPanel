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

public class FloatPanel extends FloatView implements OnClickListener {

	private Context mContext;

	public FloatPanel(Context context, WindowManager winManager) {
		super(context, winManager);
		loadWidgets();
		loadTheme();
	}

	@Override
	int onInflaterContentView() {
		return R.layout.control_panel;
	}
	
	protected int onInitDragView(){
		return R.id.header;
	}
	
    private void loadWidgets(){
    	int[] wigetList = {1,2,3};
    	LinearLayout container = (LinearLayout)(mContentView.findViewById(R.id.container));
    	container.removeAllViews();
    	IWidget widget;
    	for(int i: wigetList){
    		widget = WidgetFactory.createControlbar(mContext, i);
    		if(widget != null){
        		container.addView((View) widget);
    		}else{
    			
    		}
    	}
    	klilog.i("loadWidgets() mFloatPanel.getHeight() = "+mContentView.getHeight());
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
		View header = mContentView.findViewById(R.id.header);
		if(header != null) header.setBackgroundColor(colorHeader);
		View container = mContentView.findViewById(R.id.container);
		if(container != null) container.setBackgroundColor(colorContainer);
//		View tags = mFloatPanel.findViewById(R.id.tags);
//		if(tags != null) tags.setBackgroundColor(colorHeader);
    }
	
	@Override
	public void onClick(View arg0) {
		
	}
}
