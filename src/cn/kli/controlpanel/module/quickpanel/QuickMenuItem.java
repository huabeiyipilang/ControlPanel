package cn.kli.controlpanel.module.quickpanel;

import java.util.ArrayList;
import java.util.List;

public class QuickMenuItem {
    int level;
    int icon;
    int title;
    List<QuickMenuItem> mChildren = new ArrayList<QuickMenuItem>();
    
    public QuickMenuItem() {
    }

    public QuickMenuItem(int level, int icon, int title) {
        this.level = level;
        this.icon = icon;
        this.title = title;
    }
    
    
}
