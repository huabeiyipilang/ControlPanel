package cn.kli.controlpanel.module.quickpanel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.WindowManager;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.module.floatpanel.FloatManager;
import cn.kli.controlpanel.module.floatpanel.FloatPanelService;
import cn.kli.controlpanel.module.quickpanel.QuickPanelView.State;
import cn.kli.controlpanel.module.quickpanel.QuickPanelView.StateChangeListener;
import cn.kli.controlpanel.modules.OneKeyLockScreen;
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
        QuickMenuItem item1 = new QuickMenuItem(0, R.drawable.ic_audio_alarm, R.string.module_lock_screen);        
        item1.setOnSelectRunnable(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(mContext, OneKeyLockScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        
        QuickMenuItem item2 = new QuickMenuItem(0, R.drawable.ic_audio_alarm, R.string.module_sound);
        item2.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                FloatManager.getInstance(mContext).showPanel();
            }
        });

        List<QuickMenuItem> menuList = new ArrayList<QuickMenuItem>();
        menuList.add(item1);
        menuList.add(item2);
        
        QuickMenuItem root = new QuickMenuItem();
        root.mChildren = menuList;
        
        mQuickPanel.setMenuList(root);
    }
}
