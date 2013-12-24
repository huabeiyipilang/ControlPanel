package cn.kli.controlpanel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.res.Configuration;
import cn.kli.controlpanel.about.AboutFragment;
import cn.kli.controlpanel.module.appmanager.AppManagerFragment;
import cn.kli.controlpanel.module.quickpanel.QuickPanelManager;
import cn.kli.menuui.Config;
import cn.kli.menuui.Module;
import cn.kli.utils.KliUtils;

public class App extends Application {

	private static List<ConfigurationListener> sConfigListenerList = new ArrayList<ConfigurationListener>();
	
	private Config mModuleConfig;
	
	private QuickPanelManager mQuickPanelManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
        KliUtils.init(this);
		mModuleConfig = Config.getInstance();
		initModules();
		
		mQuickPanelManager = QuickPanelManager.getInstance(this);
		mQuickPanelManager.showQuickPanel();
		
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
