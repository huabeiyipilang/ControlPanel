package cn.kli.controlpanel.module.quickpanel;

import cn.kli.controlpanel.module.quickpanel.QuickPanel.PanelChangedListener;
import android.content.Context;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class QuickPanelManager implements PanelChangedListener {
    private static QuickPanelManager sInstance;
    
    private Context mContext;
    private WindowManager mWinManager;
    private WindowManager.LayoutParams mParams;
    private QuickPanel mQuickPanel;
    private boolean isPanelShow;
    
    private QuickPanelManager(Context context){
        mContext = context;
        mWinManager =  (WindowManager) context.getSystemService("window");
        mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mQuickPanel = new QuickPanel(mContext, this);
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
        }
    }
    
    private void updatePanelParams(){
        mWinManager.updateViewLayout(mQuickPanel, mParams);
    }
    
    public boolean isPanelShow(){
        return isPanelShow;
    }

    @Override
    public void onSizeChanged(int w, int h) {
        if(mParams != null){
            mParams.width = w;
            mParams.height = h;
            updatePanelParams();
        }
    }
}
