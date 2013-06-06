package cn.kli.controlpanel;

import java.util.ArrayList;
import java.util.List;

public class Config {
	public static List<Module> getModules(){
		List<Module> modules = new ArrayList<Module>();
		Module module = new Module();
		module.name = "light";
		module.icon = R.drawable.light_on;
		module.cls = LightControlActivity.class;
		modules.add(module);
		
		return modules;
	}
}
