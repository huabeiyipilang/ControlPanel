package cn.kli.controlpanel.module.deviceinfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import cn.kli.controlpanel.R;
import cn.kli.utils.DeviceUtils;

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

		group.addChild(new InfoChild(R.string.info_device_build_model, Build.MODEL));
		group.addChild(new InfoChild(R.string.info_device_build_cpu_abi, Build.CPU_ABI));
		group.addChild(new InfoChild(R.string.info_device_build_cpu_abi2, Build.CPU_ABI2));
		group.addChild(new InfoChild(R.string.info_device_build_serial, Build.SERIAL));
		group.addChild(new InfoChild(R.string.info_device_build_manufacturer, Build.MANUFACTURER));
		
		DisplayMetrics metric = new DisplayMetrics();
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = metric.heightPixels;
		group.addChild(new InfoChild(R.string.info_device_display_resolution, width+"*"+height));
		
		group.addChild(new InfoChild(R.string.info_device_ram_total, DeviceUtils.getmem_TOLAL()/1024+"MB"));
		
		/*
		group.addChild(new InfoChild(R.string.info_device_build_id, Build.ID));
		group.addChild(new InfoChild(R.string.info_device_build_display, Build.DISPLAY));
		group.addChild(new InfoChild(R.string.info_device_build_product, Build.PRODUCT));
		group.addChild(new InfoChild(R.string.info_device_build_device, Build.DEVICE));
		group.addChild(new InfoChild(R.string.info_device_build_board, Build.BOARD));
		group.addChild(new InfoChild(R.string.info_device_build_brand, Build.BRAND));
		group.addChild(new InfoChild(R.string.info_device_build_bootloader, Build.BOOTLOADER));
		group.addChild(new InfoChild(R.string.info_device_build_radio, Build.RADIO));
		group.addChild(new InfoChild(R.string.info_device_build_hardware, Build.HARDWARE));
		*/
		return group;
	}
	
	private InfoGroup initSystemInfo(){
		InfoGroup group = new InfoGroup();
		group.name = R.string.info_group_system;
		
		
		return group;
	}
}
