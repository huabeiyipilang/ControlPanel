package cn.kli.controlpanel.base;

import android.content.Context;

public class FloatWindowManager {
    public static void startWindow(Context context, Class<? extends BaseFloatWindow> window){
        try {
            BaseFloatWindow floatWindow = window.newInstance();
            floatWindow.setContext(context);
            floatWindow.show();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}