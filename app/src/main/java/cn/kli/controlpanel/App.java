package cn.kli.controlpanel;

import android.app.Application;

/**
 * Created by carl on 14-4-16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Init.init(this);
        Init.loadConfig(this);
    }

}
