package cn.kli.controlpanel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.kli.controlwidgets.IWidget;
import cn.kli.controlwidgets.WidgetFactory;

public class ControlPanel extends Activity implements OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("onCreate()");
        setContentView(R.layout.control_panel);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  
        initWidget();
        initTheme();
        LinearLayout body = (LinearLayout)findViewById(R.id.body);
        body.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}});
        
        ImageView setting = (ImageView)findViewById(R.id.setting);
        setting.setOnClickListener(this);
    }
    
    private void initWidget(){
    	int[] wigetList = {1,2,3};
    	LinearLayout container = (LinearLayout)findViewById(R.id.container);
    	IWidget widget;
    	for(int i: wigetList){
    		widget = WidgetFactory.createControlbar(this, i);
    		if(widget != null){
        		container.addView((View) widget);
    		}else{
    			
    		}
    	}
    }
    
    private void initTheme(){
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
		View view1 = findViewById(R.id.header);
		if(view1 != null) view1.setBackgroundColor(colorHeader);
		View view2 = findViewById(R.id.container);
		if(view1 != null) view2.setBackgroundColor(colorContainer);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.setting:
			Intent intent = new Intent(this,SettingsActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}
}
