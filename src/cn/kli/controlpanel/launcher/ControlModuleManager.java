package cn.kli.controlpanel.launcher;

import java.util.LinkedList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class ControlModuleManager {
	
	private static ControlModuleManager sInstance;
	private Context mContext;
	private final static String QUEEN_CHILD_ENTRY = "cn.kli.intent.ENABLE_ENTRY";
	
	private ControlModuleManager(Context context){
		mContext = context;
	}
	
	public static ControlModuleManager getInstance(Context context){
		if(sInstance == null){
			sInstance = new ControlModuleManager(context);
		}
		return sInstance;
	}
	
	public List<ResolveInfo> getQueenApps(){
		PackageManager pm = mContext.getPackageManager();
		Intent intent = new Intent("kli.intent.queen.tab", null);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        List<ResolveInfo> queen_apps = new LinkedList<ResolveInfo>();
		for (ResolveInfo app : apps) {
			if(app.activityInfo.packageName.startsWith("cn.kli.queen.")){
				queen_apps.add(app);
			}
		}
		return queen_apps;
	}
	
	public static void removeChildrenIcon(Context context){
		Intent intent = new Intent();
		intent.setAction(QUEEN_CHILD_ENTRY);
		intent.putExtra("enable", false);
		context.sendBroadcast(intent);
	}
	
}
