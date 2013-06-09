package cn.kli.controlpanel;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;

public class Config {
	public static List<Group> sGroupList;
	
	public static List<Group> getGroupList(){
		initGroupList();
		return sGroupList;
	}
	
	public static Group getGroupByClassName(String name){
		for(Group group : sGroupList){
			if(group.cls.getName().equals(name)){
				return group;
			}
		}
		return null;
	}
	
	private static void initGroupList(){
		if(sGroupList == null){
			sGroupList = new ArrayList<Group>();

			//Control panel group
			Group controlGroup = new Group();
			controlGroup.name = R.string.group_control_panel;
			controlGroup.cls = GroupControlFragment.class;

			//Tools group
			Group toolsGroup = new Group();
			toolsGroup.name = R.string.group_tools;
			toolsGroup.cls = GroupToolsFragment.class;
			
			//light module
			Module lightModule = new Module();
			lightModule.name = R.string.module_light;
			lightModule.icon = R.drawable.light_on;
			lightModule.cls = LightControlActivity.class;
			lightModule.setParentGroup(toolsGroup);

			//lock screen module
			//support above version 9
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
				Module loclScreenModule = new Module();
				loclScreenModule.name = R.string.module_lock_screen;
				loclScreenModule.icon = R.drawable.lock;
				loclScreenModule.cls = OneKeyLockScreen.class;
				loclScreenModule.setParentGroup(toolsGroup);
			}

			//Settings group
			Group settingsGroup = new Group();
			settingsGroup.name = R.string.group_settings;
			settingsGroup.cls = GroupSettingsFragment.class;

			//About group
			Group aboutGroup = new Group();
			aboutGroup.name = R.string.group_about;
			aboutGroup.cls = GroupAboutFragment.class;
			
			sGroupList.add(controlGroup);
			sGroupList.add(toolsGroup);
			sGroupList.add(settingsGroup);
			sGroupList.add(aboutGroup);
		}
	}
}
