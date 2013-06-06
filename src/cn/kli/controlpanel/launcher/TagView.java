package cn.kli.controlpanel.launcher;

import cn.kli.controlpanel.Module;
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
	private Module mModule;

	public TagView(Context context, Module module) {
		super(context);
		mContext = context;
		mModule = module;
//		init();
		bindApp(mModule);
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

	private void bindApp(Module module){
		setTagIcon(module.icon);
	}
	
	@Override
	public void onClick(){
		launchApp(mModule);
	}
	
	private void launchApp(Module module){
		Intent intent = new Intent(mContext, module.cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
