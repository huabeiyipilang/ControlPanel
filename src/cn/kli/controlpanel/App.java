package cn.kli.controlpanel;

import java.util.ArrayList;
import java.util.List;

import cn.kli.utils.klilog;
import android.app.Application;
import android.content.res.Configuration;

public class App extends Application {

	private static List<ConfigurationListener> sConfigListenerList = new ArrayList<ConfigurationListener>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		klilog.i("App onCreate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		for(ConfigurationListener listener : sConfigListenerList){
			listener.onConfigurationChanged(newConfig);
		}
	}
	
	public static void registerConfigListener(ConfigurationListener listener){
		sConfigListenerList.add(listener);
	}
	
	public static void unRegisterConfigListener(ConfigurationListener listener){
		sConfigListenerList.remove(listener);
	}

	public interface ConfigurationListener{
		void onConfigurationChanged(Configuration newConfig);
	}
}
