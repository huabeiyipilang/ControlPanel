package cn.kli.controlpanel;

import android.content.Context;
import android.content.Intent;

import cn.kli.controlpanel.service.MainService;
import cn.kli.controlpanel.utils.Prefs;
import cn.kli.controlpanel.utils.Statistic;

/**
 * Created by carl on 14-4-16.
 */
public class Init {
    public static void init(Context context){
        Statistic.init(context);
        Prefs.init(context);
    }

    public static void loadConfig(Context context){
        if(Prefs.getInstance().getBoolean(Prefs.BOOLEAN_SERVICE_ENABLE, true)){
            context.startService(new Intent(context, MainService.class));
        }
    }
}
