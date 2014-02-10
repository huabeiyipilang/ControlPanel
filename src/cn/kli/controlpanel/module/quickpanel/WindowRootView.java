package cn.kli.controlpanel.module.quickpanel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class WindowRootView extends FrameLayout {
    
    private OnBackKeyPressedListener mListener;

    public interface OnBackKeyPressedListener{
        void OnBackKeyPressed();
    }
    
    public WindowRootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WindowRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowRootView(Context context) {
        super(context);
    }

    public void setOnBackKeyPressedListener(OnBackKeyPressedListener listener){
        mListener = listener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(mListener != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN){
            mListener.OnBackKeyPressed();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
