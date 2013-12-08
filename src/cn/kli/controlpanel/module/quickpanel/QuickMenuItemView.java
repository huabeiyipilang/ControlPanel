package cn.kli.controlpanel.module.quickpanel;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kli.controlpanel.R;

public class QuickMenuItemView extends LinearLayout {
    
    private ImageView mIcon;
    private TextView mTitle;
    
    private Rect mRect = new Rect();;
    private QuickMenuItem mItem;
    
    public QuickMenuItemView(Context context) {
        super(context);
        init();
    }
    
    public QuickMenuItemView(Context context, QuickMenuItem item){
        super(context);
        setMenuItem(item);
        init();
    }
    
    public QuickMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public QuickMenuItem getMenuItem(){
        return mItem;
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getGlobalVisibleRect(mRect);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getGlobalVisibleRect(mRect);
    }
    
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.quick_meui_item, this, true);
        setBackgroundResource(R.color.quick_item_bg_nomarl);
        initView();
    }

    private void initView(){
        mIcon = (ImageView)findViewById(R.id.iv_icon);
        mTitle = (TextView)findViewById(R.id.tv_title);
    }
    
    public void setMenuItem(QuickMenuItem item){
        mItem = item;
        setIcon(mItem.icon);
        setTitle(mItem.title);
    }
    
    private void setTitle(int resId){
        mTitle.setText(resId);
    }
    
    private void setIcon(int resId){
        mIcon.setImageResource(resId);
    }
    
    public boolean onTouchChanged(int x, int y){
        return mRect.contains(x, y);
    }

    public void onMotionOver(boolean over){
        setBackgroundResource(over ? R.color.quick_item_bg_focus :R.color.quick_item_bg_nomarl);
    }
}
