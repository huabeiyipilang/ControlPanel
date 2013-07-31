package cn.kli.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;

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
		}  
		
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);  
		return homes.contains(rti.get(0).topActivity.getPackageName());  
	} 
	
	public static boolean isOrentationPortrait(Context context){
		Configuration config = context.getResources().getConfiguration();
		return config.orientation == Configuration.ORIENTATION_PORTRAIT;
	}
	
	public static DisplayMetrics getDisplayMetrics(Context context){
		return context.getResources().getDisplayMetrics(); 
	}
	
	public static int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
