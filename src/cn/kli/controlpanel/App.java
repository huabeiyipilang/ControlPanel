package cn.kli.controlpanel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.res.Configuration;
import cn.kli.controlpanel.about.AboutFragment;
import cn.kli.controlpanel.module.AppManager.AppManagerFragment;
import cn.kli.menuui.Config;
import cn.kli.menuui.Module;

public class App extends Application {

	private static List<ConfigurationListener> sConfigListenerList = new ArrayList<ConfigurationListener>();
	
	private Config mModuleConfig;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mModuleConfig = Config.getInstance();
		initModules();
	}
	
	private void initModules(){
		//app manager module
		Module appModule = new Module();
		appModule.name = getString(R.string.module_app_manager);
		appModule.cls = AppManagerFragment.class;
		mModuleConfig.addModule(appModule);
		
		Module adsModule = new Module();
		adsModule.name = getString(R.string.app_recommend);
		adsModule.cls = Object.class;
		mModuleConfig.addModule(adsModule);
		
		//about module
		Module aboutModule = new Module();
		aboutModule.name = getString(R.string.module_about);
		aboutModule.cls = AboutFragment.class;
		mModuleConfig.addModule(aboutModule);
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
