package cn.kli.controlpanel.module.quickpanel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.utils.VibrateUtils;
import cn.kli.utils.klilog;

class QuickPanelView extends RelativeLayout {
    private klilog log = new klilog(QuickPanelView.class);
    
    private LinearLayout mHandleView;
    private LinearLayout mPanelView;
    
    private QuickMenuItemView mFocusItem;

    private QuickMenuItem mRootMenuItem;
    private List<QuickMenuItemView> mVisibleMenuList;
    
    private IndicatorManager mIndicatorManager;
    
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
        mIndicatorManager = IndicatorManager.getInstance(getContext());
        initViews();
        switchState(State.HANDLE);
    }
    
    private void initViews(){
        mHandleView = (LinearLayout)findViewById(R.id.ll_handle);
        mHandleView.getBackground().setAlpha(100);
        mPanelView = (LinearLayout)findViewById(R.id.ll_panel);
        mIndicatorManager.setViews((LinearLayout)findViewById(R.id.ll_up),
                (LinearLayout)findViewById(R.id.ll_down));
        
        mHandleView.setOnTouchListener(new OnHandleTouchListener());
//        mPanelView.setLayoutParams(new LayoutParams(screenWidth, screenHeight));
        mIndicatorManager.start();
    }
    
    public void setStateChangeListener(StateChangeListener listener){
        mStateChangeListener = listener;
    }
    
    public void setMenuList(QuickMenuItem root){
        mRootMenuItem = root;
        
        //遍历找到菜单树的深度和宽度
        KuanduShendu calc = new KuanduShendu();
        calc.shendu(mRootMenuItem);
        
        for(int i = 0; i <= calc.shendu; i++){
            LinearLayout column = newMenuColumn();
            for(int j = 0; j < calc.kuandu; j++){
                QuickMenuItemView item = new QuickMenuItemView(getContext());
                item.setVisibility(View.INVISIBLE);
                column.addView(item);
            }
            mPanelView.addView(column);
        }
    }
    
    private LinearLayout newMenuColumn(){
        return (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.quick_menu_column, null, true);
    }
    
    private class KuanduShendu{
        int kuandu = 0;
        int shendu = 0;
        
        private void shendu(QuickMenuItem root){
            List<QuickMenuItem> children = root.mChildren;
            for(QuickMenuItem item : children){
                item.level = root.level + 1;
            }
            int shen = root.level;
            if(children != null && children.size() > 0){
                int kuan = children.size();
                if(kuan > kuandu){
                    kuandu = kuan;
                }
                for(QuickMenuItem item : children){
                    shendu(item);
                }
            }else{
                if(shen > shendu){
                    shendu = shen;
                    log.i("shendu:"+shendu);
                }
            }
        }
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
        showChildMenu(mRootMenuItem, null);
    }
    
    private void setMenuItem(QuickMenuItem item, int pos){
        if(item == null){
            return;
        }
        LinearLayout menu = getColByItem(item);
        QuickMenuItemView itemView = (QuickMenuItemView)menu.getChildAt(pos);
        itemView.post(item.getUpdateRunnable());
        itemView.setMenuItem(item);
        itemView.setVisibility(View.VISIBLE);
//        log.i("item visible:"+item);
    }
    
    private void showChildMenu(QuickMenuItem father, QuickMenuItemView fatherView) {
        //父菜单数据为Null， 错误情况，清空菜单。
        if (father == null) {
            clearMenuContainer();
        }
        //清空父菜单项的子菜单
        clearMenuContainer(father.level + 1);

        //子菜单为空，退出。
        List<QuickMenuItem> children = father.mChildren;
        if (children == null || children.size() <= 0) {
            return;
        }
        
        //菜单列最大值
        int max = ((ViewGroup) mPanelView.getChildAt(0)).getChildCount() - 1;
        //子菜单个数。
        int size = children.size();
        //第一个子菜单将要显示的位置。
        int top = 0;

        if (fatherView == null) { //根菜单的情况
            top = max - size + 1;
        } else {
            //父菜单项在菜单中的位置
            ViewGroup fatherContainer = getColByItem(father);
            int fatherIndex = fatherContainer.indexOfChild(fatherView);

            if (max - fatherIndex >= size) {
                top = fatherIndex;
            } else {
                top = max - size + 1;
            }
        }

        for (int i = 0; i < size; i++) {
            setMenuItem(children.get(i), i + top);
        }
        updateVisibleMenu();
    }
    
    private class OnHandleTouchListener implements OnTouchListener{
//        private float handleDownX;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch(mState){
            case HANDLE:
                if(action == MotionEvent.ACTION_DOWN){
                    VibrateUtils.getInstance(getContext()).vibrateShortly();
                    switchState(State.PANEL);
//                    handleDownX = event.getRawX();
                }
                /*else if(action == MotionEvent.ACTION_MOVE){
                    if(Math.abs(event.getRawX() - handleDownX) > 30){
                        switchState(State.PANEL);
                        handleDownX = 0;
                    }
                }*/
                return true;
            case PANEL:
                if(action == MotionEvent.ACTION_MOVE){
                    notifyMotion((int)event.getRawX(), (int)event.getRawY());
                }else if(action == MotionEvent.ACTION_UP){
                    if(mFocusItem != null){
                        mFocusItem.onSelect();
                    }
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
        
        if (mFocusItem != null) {
            if (mFocusItem.getMenuItem().mChildren.size() > 0) {
                showChildMenu(mFocusItem.getMenuItem(), mFocusItem);
            }else{
                clearMenuContainer(mFocusItem.getMenuItem().level + 1);
            }
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
        for(int i = 0; i < mPanelView.getChildCount(); i++){
            ViewGroup container = (ViewGroup) mPanelView.getChildAt(i);
            for(int j = 0; j < container.getChildCount(); j++){
                View view = container.getChildAt(j);
                if(view instanceof QuickMenuItemView && view.getVisibility() == View.VISIBLE){
                    list.add((QuickMenuItemView) view);
                }
            }
        }
        return list;
    }
    
    private void clearMenuContainer(){
        for(int i = 0; i < mPanelView.getChildCount(); i++){
            invisibleAllChildView((ViewGroup) mPanelView.getChildAt(i));
        }
    }
    
    private void clearMenuContainer(int level){
        if(level > mPanelView.getChildCount()){
            return;
        }
        int index = getColIndexByLevel(level);
        LinearLayout container = (LinearLayout) mPanelView.getChildAt(index);
        if(container != null){
            invisibleAllChildView(container);
        }
    }
    
    private void invisibleAllChildView(ViewGroup group){
        for(int i = 0; i < group.getChildCount(); i++){
            group.getChildAt(i).setVisibility(View.INVISIBLE);
        }
    }
    
    private LinearLayout getColByItem(QuickMenuItem item){
        int index = getColIndexByLevel(item.level);
        return (LinearLayout) mPanelView.getChildAt(index);
    }
    
    private int getColIndexByLevel(int level){
        return mPanelView.getChildCount() - 1 - level;
    }
}
