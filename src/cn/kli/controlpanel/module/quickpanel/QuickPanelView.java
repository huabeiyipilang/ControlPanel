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

    private int screenWidth;
    private int screenHeight;
    
    private LinearLayout mHandleView;
    private LinearLayout mPanelView;
    
    private LinearLayout mMenuLv1;
    private LinearLayout mMenuLv2;
    private LinearLayout mMenuLv3;
    private List<LinearLayout> mMenuContainers;
    
    private QuickMenuItemView mFocusItem;

    private QuickMenuItem mRootMenuItem;
    private List<QuickMenuItemView> mVisibleMenuList;
    
    private State mState;
    private StateChangeListener mStateChangeListener; 
    
    enum State{
        PANEL, HANDLE
    }
    
    interface StateChangeListener{
        void onStateChanged(State state);
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
    
    public void setStateChangeListener(StateChangeListener listener){
        mStateChangeListener = listener;
    }
    
    public void setMenuList(QuickMenuItem root){
        mRootMenuItem = root;
    }
    
    private void switchState(State state){
        if(mState == state){
            return;
        }
        log.i("switch state:"+mState+" => "+state);
        mState = state;
        notifyStateChanged();
        switch(mState){
        case HANDLE:
            mPanelView.setVisibility(View.GONE);
            mHandleView.setVisibility(View.VISIBLE);
            clearMenuContainer();
            break;
        case PANEL:
            mHandleView.setVisibility(View.GONE);
            mPanelView.setVisibility(View.VISIBLE);
            startMenu();
            break;
        }
    }
    
    private void startMenu(){
        showChildMenu(mRootMenuItem);
    }
    
    private void addMenuItem(QuickMenuItem item){
        if(item == null){
            return;
        }
        LinearLayout menu = mMenuContainers.get(item.level);
        QuickMenuItemView itemView = new QuickMenuItemView(getContext(), item);
        menu.addView(itemView);
        log.i("add View at:"+item.level);
    }
    
    private void showChildMenu(QuickMenuItem father){
        if(father == null){
            clearMenuContainer();
        }
        clearMenuContainer(father.level + 1);
        List<QuickMenuItem> children = father.mChildren;
        if(children != null && father.mChildren.size() > 0){
            for(QuickMenuItem item : father.mChildren){
                addMenuItem(item);
            }
        }
        updateVisibleMenu();
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
                    notifyMotion((int)event.getRawX(), (int)event.getRawY());
                }else if(action == MotionEvent.ACTION_UP){
                    switchState(State.HANDLE);
                }
                return true;
            }
            return false;
        }
    }
    
    private void notifyStateChanged(){
        if(mStateChangeListener != null){
            mStateChangeListener.onStateChanged(mState);
        }
    }
    
    private void notifyMotion(int x, int y){
        if(mFocusItem == null){
            mFocusItem = notifyAllVisibleItemTouchChanged(x, y);
        }else{
            boolean res = mFocusItem.onTouchChanged(x, y);
            if(!res){
                mFocusItem = null;
            }
        }
        
        if(mFocusItem != null && mFocusItem.getMenuItem().mChildren.size() > 0){
            showChildMenu(mFocusItem.getMenuItem());
        }
    }
    
    /**
     * 遍历所有可见menu，直到找到焦点。返回焦点
     * @Title: notifyAllVisibleItem
     * @param x
     * @param y
     * @return
     * @return QuickMenuItemView
     * @date 2013-12-9 上午11:03:27
     */
    private QuickMenuItemView notifyAllVisibleItemTouchChanged(int x, int y){
        for(QuickMenuItemView item : mVisibleMenuList){
            if(item.onTouchChanged(x, y)){
                return item;
            }
        }
        return null;
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
    
    private void clearMenuContainer(){
        for(LinearLayout container : mMenuContainers){
            container.removeAllViews();
        }
    }
    
    private void clearMenuContainer(int level){
        LinearLayout container = mMenuContainers.get(level);
        if(container != null){
            container.removeAllViews();
        }
    }
}
