package cn.kli.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class UIUtils {
    
	public static void addShortcut(Context context, Intent intent, int name, int icon) {
		 
        String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        // 快捷方式要启动的包

        // 设置快捷方式的参数
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        // 设置名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
                        .getString(name)); // 设置启动 Intent
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 设置图标
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(context,
                        		icon));
        // 只创建一次快捷方式
        shortcutIntent.putExtra("duplicate", false);
        // 创建
        context.sendBroadcast(shortcutIntent);
	}
	
	
	public static boolean isHome(Context context){ 
		List<String> homes = new ArrayList<String>();  
		PackageManager packageManager = context.getPackageManager();  
		Intent intent = new Intent(Intent.ACTION_MAIN);  
		intent.addCategory(Intent.CATEGORY_HOME);  
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,  
				PackageManager.MATCH_DEFAULT_ONLY);  
		for(ResolveInfo info : resolveInfo){  
			homes.add(info.activityInfo.packageName);  
			System.out.println(info.activityInfo.packageName);  
		}  
		
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);  
		return homes.contains(rti.get(0).topActivity.getPackageName());  
	} 
}
