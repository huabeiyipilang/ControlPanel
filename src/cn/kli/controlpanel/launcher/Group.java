package cn.kli.controlpanel.launcher;

import java.util.ArrayList;
import java.util.List;


public class Group {
	public int name;
	public Class<?> cls;
	private List<Module> children = new ArrayList<Module>();
	
	public void addChild(Module module){
		if(!children.contains(module)){
			module.parent = this;
			children.add(module);
		}
	}
	
	public List<Module> getModuleList(){
		return children;
	}
}
