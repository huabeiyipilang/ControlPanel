package cn.kli.controlpanel.launcher;

import java.util.ArrayList;
import java.util.List;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.R.drawable;
import cn.kli.controlpanel.R.string;
import cn.kli.controlpanel.floatpanel.FloatPanelLauncher;
import cn.kli.controlpanel.module.devicecontrol.AppControl;
import cn.kli.controlpanel.module.devicecontrol.BluetoothControl;
import cn.kli.controlpanel.module.devicecontrol.DateControl;
import cn.kli.controlpanel.module.devicecontrol.DisplayControl;
import cn.kli.controlpanel.module.devicecontrol.SoundControl;
import cn.kli.controlpanel.module.devicecontrol.WifiControl;
import cn.kli.controlpanel.module.deviceinfo.DeviceInfoActivity;
import cn.kli.controlpanel.module.feedback.ComposeMessageActivity;
import cn.kli.controlpanel.module.t9search.T9MainActivity;
import cn.kli.controlpanel.modules.LightControlActivity;
import cn.kli.controlpanel.modules.OneKeyLockScreen;

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

			//display module
			Module displayModule = new Module();
			displayModule.name = R.string.display;
			displayModule.icon = R.drawable.light_on;
			displayModule.cls = DisplayControl.class;
			displayModule.setParentGroup(controlGroup);

			//app module
			Module appModule = new Module();
			appModule.name = R.string.display;
			appModule.icon = R.drawable.light_on;
			appModule.cls = AppControl.class;
			appModule.setParentGroup(controlGroup);

			//bt module
			Module btModule = new Module();
			btModule.name = R.string.display;
			btModule.icon = R.drawable.light_on;
			btModule.cls = BluetoothControl.class;
			btModule.setParentGroup(controlGroup);

			//date module
			Module dateModule = new Module();
			dateModule.name = R.string.display;
			dateModule.icon = R.drawable.light_on;
			dateModule.cls = DateControl.class;
			dateModule.setParentGroup(controlGroup);

			//sound module
			Module soundModule = new Module();
			soundModule.name = R.string.display;
			soundModule.icon = R.drawable.light_on;
			soundModule.cls = SoundControl.class;
			soundModule.setParentGroup(controlGroup);

			//wifi module
			Module wifiModule = new Module();
			wifiModule.name = R.string.display;
			wifiModule.icon = R.drawable.light_on;
			wifiModule.cls = WifiControl.class;
			wifiModule.setParentGroup(controlGroup);
			
			//*********************************************
			//Tools group
			Group toolsGroup = new Group();
			toolsGroup.name = R.string.group_tools;
			toolsGroup.cls = GroupToolsFragment.class;

			//float module
			Module floatModule = new Module();
			floatModule.name = R.string.module_float_panel;
			floatModule.icon = R.drawable.float_launcher;
			floatModule.cls = FloatPanelLauncher.class;
			floatModule.setParentGroup(toolsGroup);

			//t9 module
			Module t9Module = new Module();
			t9Module.name = R.string.quick_app_list;
			t9Module.icon = R.drawable.t9_launcher;
			t9Module.cls = T9MainActivity.class;
			t9Module.setParentGroup(toolsGroup);
			
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
			//Feedback group
			Group feedbackGroup = new Group();
			feedbackGroup.name = R.string.module_feedback;
			feedbackGroup.cls = GroupFeedbackFragment.class;

			//feedback1 module
			Module feedback1Module = new Module();
			feedback1Module.name = R.string.feedback1;
			feedback1Module.icon = R.drawable.ic_paper;
			feedback1Module.cls = ComposeMessageActivity.class;
			feedback1Module.setParentGroup(feedbackGroup);

			//feedback2 module
			Module feedback2Module = new Module();
			feedback2Module.name = R.string.feedback2;
			feedback2Module.icon = R.drawable.ic_paper;
			feedback2Module.cls = ComposeMessageActivity.class;
			feedback2Module.setParentGroup(feedbackGroup);

			//feedback1 module
			Module feedback3Module = new Module();
			feedback3Module.name = R.string.feedback3;
			feedback3Module.icon = R.drawable.ic_paper;
			feedback3Module.cls = ComposeMessageActivity.class;
			feedback3Module.setParentGroup(feedbackGroup);
			
			//*********************************************
			//About group
			Group aboutGroup = new Group();
			aboutGroup.name = R.string.group_about;
			aboutGroup.cls = GroupAboutFragment.class;
			
//			sGroupList.add(controlGroup);
			sGroupList.add(toolsGroup);
			sGroupList.add(settingsGroup);
			sGroupList.add(feedbackGroup);
			sGroupList.add(aboutGroup);
		}
	}
}
