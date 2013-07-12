package cn.kli.controlpanel;

import java.util.ArrayList;
import java.util.List;

import cn.kli.controlpanel.module.deviceinfo.DeviceInfoActivity;

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
	/*
	 * <string name="action_wifi">android.settings.WIFI_SETTINGS</string>
    <string name="action_bt">android.settings.BLUETOOTH_SETTINGS</string>
    <string name="action_sound">android.settings.SOUND_SETTINGS</string>
    <string name="action_app">android.settings.APPLICATION_SETTINGS</string>
    <string name="action_date">android.settings.DATE_SETTINGS</string>
	 */
	
	private static void initGroupList(){
		if(sGroupList == null){
			sGroupList = new ArrayList<Group>();

			//*********************************************
			//Control panel group
			Group controlGroup = new Group();
			controlGroup.name = R.string.group_control_panel;
			controlGroup.cls = GroupControlFragment.class;

			//device info module
			Module deviceInfoModule = new Module();
			deviceInfoModule.name = R.string.device_info_group;
			deviceInfoModule.icon = R.drawable.light_on;
			deviceInfoModule.cls = DeviceInfoActivity.class;
			deviceInfoModule.setParentGroup(controlGroup);

			//device info module
			Module displayModule = new Module();
			displayModule.name = R.string.display;
			displayModule.icon = R.drawable.light_on;
			displayModule.cls = DisplayControl.class;
			displayModule.setParentGroup(controlGroup);
			
			//*********************************************
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

			//*********************************************
			//Settings group
			Group settingsGroup = new Group();
			settingsGroup.name = R.string.group_settings;
			settingsGroup.cls = GroupSettingsFragment.class;

			//*********************************************
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
