package cn.kli.controlpanel.module.quickpanel;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.about.AboutWindow;
import cn.kli.controlpanel.module.quickpanel.QuickPanelView.State;
import cn.kli.controlpanel.module.quickpanel.QuickPanelView.StateChangeListener;
import cn.kli.controlpanel.settings.SettingsWindow;
import cn.kli.utils.klilog;

public class QuickPanelManager{
    private klilog log = new klilog(QuickPanelManager.class);
    private static QuickPanelManager sInstance;
    
    private Context mContext;
    private WindowManager mWinManager;
    private QuickPanelView mQuickPanel;
    private WindowManager.LayoutParams mParams;
    private boolean isPanelShow;
    
    private QuickPanelManager(Context context){
        mContext = context;
        mWinManager =  (WindowManager) context.getSystemService("window");
        mQuickPanel = new QuickPanelView(mContext);
        initParams();
        initMenuList();
        mQuickPanel.setStateChangeListener(new StateChangeListener() {
            
            @Override
            public void onStateChanged(State state) {
                switch(state){
                case HANDLE:
                    mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    mParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                    break;
                case PANEL:
                    mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    mParams.gravity = Gravity.CENTER;
                    break;
                }
                updateQuickPanel();
            }
        });
    }

    public static QuickPanelManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new QuickPanelManager(context);
        }
        return sInstance;
    }
    
    public void showQuickPanel(){
        if(!isPanelShow()){
            mWinManager.addView(mQuickPanel, mParams);
            isPanelShow = true;
        }
    }
    
    private void updateQuickPanel(){
        if(isPanelShow()){
            mWinManager.updateViewLayout(mQuickPanel, mParams);
        }
    }
    
    public boolean isPanelShow(){
        return isPanelShow;
    }
    
    private void initParams() {

        //init params
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//      mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.format = 1;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN | 
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
    }
    
    private void initMenuList(){
        QuickMenuItem root = new QuickMenuManager(mContext).getMenuFromConfig();
        mQuickPanel.setMenuList(root);
    }

}
