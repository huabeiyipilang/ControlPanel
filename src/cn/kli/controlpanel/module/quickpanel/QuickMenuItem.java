package cn.kli.controlpanel.module.quickpanel;

import java.util.ArrayList;
import java.util.List;

public class QuickMenuItem {
    int level;
    int icon;
    int title;
    List<QuickMenuItem> mChildren = new ArrayList<QuickMenuItem>();

    private Runnable mSelectedRunable;
    
    public QuickMenuItem() {
    }

    public QuickMenuItem(int level, int icon, int title) {
        this.level = level;
        this.icon = icon;
        this.title = title;
    }
    
    public void setOnSelectRunnable(Runnable runnable){
        mSelectedRunable = runnable;
    }
    
    public Runnable getOnSelectRunnable(){
        return mSelectedRunable;
    }

    @Override
    public String toString() {
        return "title:"+title+", level:"+level+", children:"+mChildren.size();
    }
    
    
}
