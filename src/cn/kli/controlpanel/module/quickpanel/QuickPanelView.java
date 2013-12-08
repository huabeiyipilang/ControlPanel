package cn.kli.controlpanel.module.quickpanel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.kli.controlpanel.R;
import cn.kli.utils.DeviceUtils;
import cn.kli.utils.klilog;

class QuickPanelView extends RelativeLayout {
    private klilog log = new klilog(QuickPanelView.class);

    private WindowManager.LayoutParams mPanelParams;
    private int screenWidth;
    private int screenHeight;
    
    private LinearLayout mHandleView;
    private LinearLayout mPanelView;
    
    private LinearLayout mMenuLv1;
    private LinearLayout mMenuLv2;
    private LinearLayout mMenuLv3;
    private List<LinearLayout> mMenuContainers;
    
    private QuickMenuItemView mFocusItem;

    private List<QuickMenuItem> mMenuList;
    private List<QuickMenuItemView> mVisibleMenuList;
    
    private State mState;
    
    enum State{
        PANEL, HANDLE
    }
    
    public QuickPanelView(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.quick_panel, this, true);
        DeviceUtils utils = new DeviceUtils(getContext());
        DisplayMetrics metrics = utils.getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        initViews();
        switchState(State.HANDLE);
    }
    
    private void initViews(){
        mHandleView = (LinearLayout)findViewById(R.id.ll_handle);
        mPanelView = (LinearLayout)findViewById(R.id.ll_panel);
        mMenuLv1 = (LinearLayout)findViewById(R.id.ll_menulist_lv1);
        mMenuLv2 = (LinearLayout)findViewById(R.id.ll_menulist_lv2);
        mMenuLv3 = (LinearLayout)findViewById(R.id.ll_menulist_lv3);
        mMenuContainers = new ArrayList<LinearLayout>();
        mMenuContainers.add(mMenuLv1);
        mMenuContainers.add(mMenuLv2);
        mMenuContainers.add(mMenuLv3);
        
        mHandleView.setOnTouchListener(new OnHandleTouchListener());
//        mPanelView.setLayoutParams(new LayoutParams(screenWidth, screenHeight));
    }
    
    public void setMenuList(List<QuickMenuItem> list){
        mMenuList = list;
    }
    
    private void switchState(State state){
        if(mState == state){
            return;
        }
        log.i("switch state:"+mState+" => "+state);
        mState = state;
        switch(mState){
        case HANDLE:
            mPanelView.setVisibility(View.GONE);
            mHandleView.setVisibility(View.VISIBLE);
            startMenu();
            break;
        case PANEL:
            mHandleView.setVisibility(View.GONE);
            mPanelView.setVisibility(View.VISIBLE);
            break;
        }
    }
    
    private void startMenu(){
        for(QuickMenuItem item : mMenuList){
            addMenuItem(item);
        }
        updateVisibleMenu();
    }
    
    private void addMenuItem(QuickMenuItem item){
        LinearLayout menu = mMenuContainers.get(item.level);
        QuickMenuItemView itemView = new QuickMenuItemView(getContext(), item);
        menu.addView(itemView);
    }
    
    private class OnHandleTouchListener implements OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch(mState){
            case HANDLE:
                if(action == MotionEvent.ACTION_DOWN){
                    switchState(State.PANEL);
                }
                return true;
            case PANEL:
                if(action == MotionEvent.ACTION_MOVE){
                    
                }else if(action == MotionEvent.ACTION_UP){
                    switchState(State.HANDLE);
                }
                return true;
            }
            return false;
        }
    }
    
    private void notifyMotion(int x, int y){
        if(mFocusItem == null){
            //遍历可见菜单，找出focus
            //focus 空返回
            //focus 非空 传递
        }else{
//            mFocusItem.onMotionOver(x, y);
        }
    }
    
    private void updateVisibleMenu(){
        mVisibleMenuList = getMenuItems();
    }
    
    private List<QuickMenuItemView> getMenuItems(){
        List<QuickMenuItemView> list = new ArrayList<QuickMenuItemView>();
        for(LinearLayout container : mMenuContainers){
            for(int i = 0; i < container.getChildCount(); i++){
                View view = container.getChildAt(i);
                if(view instanceof QuickMenuItemView){
                    list.add((QuickMenuItemView) view);
                }
            }
        }
        return list;
    }
}
