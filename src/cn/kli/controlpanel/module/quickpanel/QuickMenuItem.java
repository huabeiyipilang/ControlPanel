package cn.kli.controlpanel.module.quickpanel;

import java.util.ArrayList;
import java.util.List;

public class QuickMenuItem {
    int level;
    int icon;
    String title;
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
    
    @Override
    public String toString() {
        return "title:"+title+", level:"+level+", children:"+mChildren.size();
    }
    
    
}
