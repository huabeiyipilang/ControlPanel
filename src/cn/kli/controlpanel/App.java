package cn.kli.controlpanel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import cn.kli.controlpanel.about.AboutFragment;
import cn.kli.controlpanel.module.appsmanager.AppManagerFragment;
import cn.kli.controlpanel.module.quickpanel.QuickPanelService;
import cn.kli.menuui.Config;
import cn.kli.menuui.Module;
import cn.kli.utils.KliUtils;

public class App extends Application {

	private static List<ConfigurationListener> sConfigListenerList = new ArrayList<ConfigurationListener>();
	
	private Config mModuleConfig;
	private Prefs mPrefs;
	private Statistic mStatistic;
	
	@Override
	public void onCreate() {
		super.onCreate();
        KliUtils.init(this);
        mStatistic = Statistic.init(this);
        mPrefs = Prefs.init(this);
		mModuleConfig = Config.getInstance();
		initModules();
		
		Intent intent = new Intent(this, QuickPanelService.class);
		intent.putExtra(QuickPanelService.SERVICE_CMD, QuickPanelService.MSG_SHOW_QUICK_PANEL);
		startService(intent);
	}
	
	private void initModules(){
		//app manager module
		Module appModule = new Module();
		appModule.name = getString(R.string.module_app_manager);
		appModule.cls = AppManagerFragment.class;
		mModuleConfig.addModule(appModule);
		
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
