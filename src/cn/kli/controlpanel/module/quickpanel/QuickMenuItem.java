package cn.kli.controlpanel.module.quickpanel;

import java.util.ArrayList;
import java.util.List;

class QuickMenuItem {
    final static int TOGGLE_DISABLE = 0;
    final static int TOGGLE_ON = 1;
    final static int TOGGLE_OFF = 2;
    
    int level;
    int icon;
    String title;
    int toggle; // 0:diable 1:on 2:off
    List<QuickMenuItem> mChildren = new ArrayList<QuickMenuItem>();

    private Runnable mSelectedRunable;
    private Runnable mUpdateRunable;
    
    public QuickMenuItem() {
    }

    public QuickMenuItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }
    
    public void setOnSelectRunnable(Runnable runnable){
        mSelectedRunable = runnable;
    }
    
    public Runnable getOnSelectRunnable(){
        return mSelectedRunable;
    }
    
    public void setUpdateRunnable(Runnable runnable){
        mUpdateRunable = runnable;
    }
    
    public Runnable getUpdateRunnable(){
        return mUpdateRunable;
    }
    
    public void setToggle(boolean on){
        toggle = on ? TOGGLE_ON : TOGGLE_OFF;
    }
    
    @Override
    public String toString() {
        return "title:"+title+", level:"+level+", children:"+mChildren.size();
    }
    
    
}
