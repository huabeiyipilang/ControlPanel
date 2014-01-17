package cn.kli.controlpanel.module.quickpanel;

import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.internal.nineoldandroids.animation.ValueAnimator;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.utils.VibrateUtils;
import cn.kli.utils.klilog;

public class QuickMenuItemView extends LinearLayout {
    private static final klilog log = new klilog(QuickMenuItemView.class);
    
    private ImageView mIcon;
    private TextView mTitle;
    private CheckBox mToggle;
    
    private Rect mRect = new Rect();
    private QuickMenuItem mItem;
    
    private boolean mFocused;
    private boolean mLastFocused;
    private ValueAnimator mFocuseAnim;
    
    public QuickMenuItemView(Context context) {
        super(context);
        init();
    }
    
    public QuickMenuItemView(Context context, QuickMenuItem item){
        super(context);
        init();
        setMenuItem(item);
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
        LayoutInflater.from(getContext()).inflate(R.layout.quick_menu_item, this, true);
        initView();
        setBackgroundColor(getResources().getColor(R.color.quick_item_bg_nomarl));
    }

    private void initView(){
        mIcon = (ImageView)findViewById(R.id.iv_icon);
        mTitle = (TextView)findViewById(R.id.tv_title);
        mToggle = (CheckBox)findViewById(R.id.cb_toggle);
        mFocuseAnim = ObjectAnimator.ofInt(this, "backgroundColor", 
                getResources().getColor(R.color.quick_item_bg_nomarl), getResources().getColor(R.color.quick_item_bg_focus));
        mFocuseAnim.setDuration(200);
    }
    
    public void setMenuItem(QuickMenuItem item){
        mItem = item;
        if(mItem.toggle == 0){
            mIcon.setVisibility(View.VISIBLE);
            mToggle.setVisibility(View.GONE);
            setIcon(mItem.icon);
        }else{
            mIcon.setVisibility(View.GONE);
            mToggle.setVisibility(View.VISIBLE);
            mToggle.setChecked(mItem.toggle == 1);
        }
        setTitle(mItem.title);
    }
    
    private void setTitle(String title){
        mTitle.setText(title);
    }
    
    private void setIcon(int resId){
        mIcon.setImageResource(resId);
    }
    
    public boolean isTouchOver(int x, int y){
        return mRect.contains(x, y);
    }
    
    public boolean onTouchChanged(int x, int y){
        getGlobalVisibleRect(mRect);
        boolean res = mRect.contains(x, y);
        mLastFocused = mFocused;
        mFocused = res;
        onMotionOver(res);
        return res;
    }

    private void onMotionOver(boolean over){
//        setBackgroundResource(over ? R.color.quick_item_bg_focus :R.color.quick_item_bg_nomarl);
        
        if(!mLastFocused && over){
            VibrateUtils.getInstance(getContext()).vibrateShortly();
            mFocuseAnim.start();
        }else if(mLastFocused && !over){
            mFocuseAnim.reverse();
        }
    }
    
    public void onSelect(){
        Runnable runnable = mItem.getOnSelectRunnable();
        if(runnable != null){
            post(runnable);
        }

        Runnable updaterunable = mItem.getUpdateRunnable();
        if(updaterunable != null){
            post(updaterunable);
        }
    }
}
