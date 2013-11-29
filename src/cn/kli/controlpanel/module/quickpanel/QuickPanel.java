package cn.kli.controlpanel.module.quickpanel;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import cn.kli.controlpanel.R;
import cn.kli.utils.DeviceUtils;

class QuickPanel extends LinearLayout {
    
    private boolean isOpen;
    private DeviceUtils mUtils;
    
    private LayoutParams mOpenParams;
    private LayoutParams mCloseParams;
    
    private PanelChangedListener mListener;
    
    interface PanelChangedListener{
        void onSizeChanged(int w, int h);
    }

    public QuickPanel(Context context, PanelChangedListener listener){
        super(context);
        mListener = listener;
        mUtils = new DeviceUtils();
        LayoutInflater.from(context).inflate(R.layout.quick_panel, this, true);
        initParams();
        
        this.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isOpen()){
                    close();
                }else{
                    open();
                }
            }
            
        });
        updateView();
    }
    
    private void initParams(){
        DisplayMetrics metrics = mUtils.getDisplayMetrics();
        mOpenParams = new LinearLayout.LayoutParams((int)metrics.xdpi, (int)metrics.ydpi);
        mCloseParams = new LinearLayout.LayoutParams((int)(metrics.xdpi / 10), (int)(metrics.ydpi / 10));
    }

    public boolean isOpen(){
        return isOpen;
    }
    
    public void open(){
        isOpen = true;
        updateView();
    }
    
    public void close(){
        isOpen = false;
        updateView();
    }
    
    private void updateView(){
        if(isOpen){
            this.setLayoutParams(mOpenParams);
        }else{
            this.setLayoutParams(mCloseParams);
        }
        notifySizeChanged();
    }
    
    private void notifySizeChanged(){
        if(mListener != null){
            LayoutParams params = (LayoutParams) getLayoutParams();
            mListener.onSizeChanged(params.width, params.height);
        }
    }
    
}
