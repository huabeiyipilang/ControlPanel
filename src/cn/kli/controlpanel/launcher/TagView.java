package cn.kli.controlpanel.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;


public class TagView extends BaseTagView {
	private Context mContext;
	private ResolveInfo mResolveInfo;

	public TagView(Context context, ResolveInfo info) {
		super(context);
		mContext = context;
		mResolveInfo = info;
//		init();
		bindApp(mResolveInfo);
	}
	
	private void init(){
		DisplayMetrics dm = new DisplayMetrics();   
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);  
		int size = dm.widthPixels/3;
		LayoutParams params = (LayoutParams) getLayoutParams();
		if(params == null){
			params = new LayoutParams(size, size);
		}
		this.setLayoutParams(params);
	}

	private void bindApp(ResolveInfo info){
		PackageManager pm = mContext.getPackageManager();
		String name = info.loadLabel(pm).toString();
		setTagName(name);
		Drawable icon = info.loadIcon(pm);
		setTagIcon(icon);
	}
	
	@Override
	public void onClick(){
		launchApp(mResolveInfo);
	}
	
	private void launchApp(ResolveInfo reInfo){
		String packageName = reInfo.activityInfo.packageName;
		String name = reInfo.activityInfo.name;
		ComponentName cn = new ComponentName(packageName,name);
		Intent intent = new Intent();
		intent.setComponent(cn);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
