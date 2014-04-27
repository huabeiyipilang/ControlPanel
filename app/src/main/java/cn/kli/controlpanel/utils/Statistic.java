package cn.kli.controlpanel.utils;

import android.content.Context;

public class Statistic {
    private static Statistic sInstance;
    
    private Context mContext;
    
    private Statistic(Context context){
        mContext = context;
    }
    
    public static Statistic init(Context context){
        if(sInstance == null){
            sInstance = new Statistic(context);
        }
        return sInstance;
    }
    
    public static Statistic getInstance(){
        return sInstance;
    }
    
    public static void onEvent(String id, String tag){
        getInstance().commitEvent(id, tag);
    }
    
    private void commitEvent(String id, String tag){

    }
}
