package cn.kli.controlpanel.module.floatpanel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import cn.kli.controlpanel.R;
import cn.kli.controlwidgets.ASwitch;
import cn.kli.controlwidgets.IWidget;
import cn.kli.controlwidgets.WidgetFactory;
import cn.kli.utils.klilog;

public class FloatPanel extends FloatView implements OnClickListener,
	OnItemClickListener, OnItemLongClickListener{

	private Context mContext;

	public FloatPanel(Context context) {
		super(context);
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.control_panel, null);
		this.setContentView(contentView);
		mContext = context;
		loadWidgets();
		loadTheme();
		loadEvent();
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
    	int[] switchList = {};
    	final SwitchAdapter switchAdapter = new SwitchAdapter(mContext, switchList);
    	GridView switchContainer = (GridView)(getContentView().findViewById(R.id.gv_switch_container));
    	switchContainer.setSelector(new ColorDrawable(Color.TRANSPARENT));
    	switchContainer.setAdapter(switchAdapter);
    	switchContainer.setOnItemClickListener(this);
    	switchContainer.setOnItemLongClickListener(this);
    	
    	int[] wigetList = {1,2,3};
    	LinearLayout barsContainer = (LinearLayout)(getContentView().findViewById(R.id.ll_bars_container));
    	barsContainer.removeAllViews();
    	IWidget widget;
    	for(int i: wigetList){
    		widget = WidgetFactory.createControlbar(mContext, i);
    		if(widget != null){
    			barsContainer.addView((View) widget);
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
		/*
		case R.id.title:
			closePanel();
			Intent titleIntent = new Intent(mContext, ControlActivity.class);
			titleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(titleIntent);
			break;
			*/
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		klilog.info("switch item onclick: position = "+position);
		((ASwitch)view).onClick();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int position,
			long id) {
		klilog.info("switch item onlongclick: position = "+position);
		return false;
	}

	@Override
	public void closePanel() {
		super.closePanel();
		FloatPanelService.showIndicator(mContext);
	}
	
	
}
