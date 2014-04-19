package cn.kli.controlpanel;

import android.content.Context;

import cn.kli.controlpanel.utils.Prefs;

/**
 * Created by carl on 14-4-16.
 */
public class Init {
    public static void init(Context context){
        Prefs.init(context);
    }
}
