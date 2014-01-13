package cn.kli.controlpanel.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import cn.kli.controlpanel.R;

abstract public class SettingItem extends LinearLayout{

	public SettingItem(Context context) {
		super(context);
        onViewInit();
	}

	public SettingItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onViewInit();
        loadAttrs(attrs);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        onViewInit();
        loadAttrs(attrs);
    }
    
    protected void loadAttrs(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItem);
        String textSize = a.getString(R.styleable.SettingItem_setting_title);
        onTitleSet(textSize);
    }
    
    protected void onTitleSet(String title){
        
    }
    
    abstract void onViewInit();
    
    protected void setTitle(int res) {
	}
	
	protected void setTitle(String res) {
	}
	
	protected void onItemClick(){
		
	}
}
