package cn.kli.controlpanel.module.deviceinfo;

import java.util.ArrayList;
import java.util.List;

import cn.kli.controlpanel.R;

import android.content.Context;

public class DeviceInfoManager {
	private Context mContext;
	private List<InfoGroup> mGroups;
	
	public DeviceInfoManager(Context context){
		mContext = context;
	}
	
	public List<InfoGroup> getGroupList(){
		if(mGroups == null){
			mGroups = new ArrayList<InfoGroup>();
			
			//硬件信息
			InfoGroup group = initHardwareInfo();
			if(group != null){
				mGroups.add(group);
			}
			
			//系统信息
			group = initSystemInfo();
			if(group != null){
				mGroups.add(group);
			}
		}
		return mGroups;
	}
	
	private InfoGroup initHardwareInfo(){
		InfoGroup group = new InfoGroup();
		group.name = R.string.info_group_hardware;
		
		InfoChild androidVersion = new InfoChild();
		return group;
	}
	
	private InfoGroup initSystemInfo(){
		InfoGroup group = new InfoGroup();
		group.name = R.string.info_group_system;
		return group;
	}
}
