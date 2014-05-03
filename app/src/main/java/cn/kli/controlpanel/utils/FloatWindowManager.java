package cn.kli.controlpanel.utils;

import android.content.Context;

import java.util.LinkedList;

import cn.kli.controlpanel.framework.BaseFloatWindow;
import cn.kli.controlpanel.indicator.IndicatorView;

public class FloatWindowManager {
    private static FloatWindowManager sInstance;

    private Context mContext;
    private LinkedList<BaseFloatWindow> mWindowStack;

    private FloatWindowManager(Context context){
        mContext = context;
        mWindowStack = new LinkedList<BaseFloatWindow>();
    }

    public static FloatWindowManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new FloatWindowManager(context);
        }
        return sInstance;
    }

    public void openWindow(Class<? extends BaseFloatWindow> window){
        IndicatorView.getsInstance(mContext).hide();
        try {
            BaseFloatWindow top = mWindowStack.peek();
            if(top != null && top.isVisibility()){
                top.hide();
            }

            BaseFloatWindow floatWindow = window.newInstance();
            floatWindow.setContext(mContext);
            mWindowStack.addFirst(floatWindow);
            floatWindow.show();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void replaceWindow(Class<? extends BaseFloatWindow> window){
        IndicatorView.getsInstance(mContext).hide();
        BaseFloatWindow top = mWindowStack.poll();
        if(top != null && top.isVisibility()){
            top.hide();
        }

        BaseFloatWindow floatWindow = null;
        try {
            floatWindow = window.newInstance();
            floatWindow.setContext(mContext);
            mWindowStack.addFirst(floatWindow);
            floatWindow.show();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public boolean backStack(){
        BaseFloatWindow top = mWindowStack.poll();
        if(top != null && top.isVisibility()){
            top.hide();
        }

        BaseFloatWindow newTop = mWindowStack.peek();
        if(newTop == null){
            IndicatorView.getsInstance(mContext).show();
            return false;
        }else {
            newTop.show();
        }
        return true;
    }
}
