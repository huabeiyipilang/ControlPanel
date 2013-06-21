package cn.kli.controlpanel;

import cn.kli.utils.klilog;
import android.app.Application;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		klilog.i("App onCreate");
	}

}
