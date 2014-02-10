package cn.kli.controlpanel.base;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.module.quickpanel.WindowRootView;
import cn.kli.controlpanel.module.quickpanel.WindowRootView.OnBackKeyPressedListener;

public class BaseFloatWindow{

    private WindowManager mWinManager;
    private WindowManager.LayoutParams mParams;
    private WindowRootView mRootView;
    private ViewGroup mWindowView;
    private ViewGroup mContentView;
    private Context mContext;
    
    void setContext(Context context){
        mContext = context;
        mRootView = (WindowRootView)LayoutInflater.from(context).inflate(R.layout.window_base, null);
        mWindowView = (ViewGroup)mRootView.findViewById(R.id.ll_window);
        mRootView.setOnBackKeyPressedListener(new OnBackKeyPressedListener() {
            
            @Override
            public void OnBackKeyPressed() {
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
                anim.setAnimationListener(new AnimationListener(){

                    @Override
                    public void onAnimationStart(Animation animation) {
                        
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        hide();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        
                    }
                    
                });
                mWindowView.startAnimation(anim);
            }
        });
        mWinManager = (WindowManager) mContext.getSystemService("window");
        initParams();
        onCreate();
    }
    
    protected void onCreate(){
        
    }
    
    protected void onStart(){
        
    }
    
    protected void onStop(){
        
    }
    
    protected Context getContext(){
        return mContext;
    }
    
    protected void setContentView(View view){
        getContentView().addView(view);
    }
    
    protected void setContentView(int resId){
        LayoutInflater.from(mContext).inflate(resId, getContentView());
    }
    
    protected void setTitle(String title){
        ((TextView)mRootView.findViewById(R.id.tv_window_title)).setText(title);
    }
    
    protected ViewGroup getContentView(){
        if(mContentView == null){
            mContentView = (ViewGroup)mRootView.findViewById(R.id.fl_content);
        }
        return mContentView;
    }
    
    protected View findViewById(int resId){
        return getContentView().findViewById(resId);
    }
    
    protected String getString(int resId){
        return mContext.getString(resId);
    }

    private void initParams() {
        // init params
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.format = 1;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.gravity = Gravity.CENTER;
    }
    
    public void show(){
        if(mWinManager != null && mParams != null){
            try {
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                anim.setAnimationListener(new AnimationListener(){

                    @Override
                    public void onAnimationStart(Animation animation) {
                        
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        onStart();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        
                    }
                    
                });
                mWinManager.addView(mRootView, mParams);
                mWindowView.startAnimation(anim);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public void hide(){
        if(mWinManager != null){
            try {
                mWinManager.removeView(mRootView);
                onStop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
