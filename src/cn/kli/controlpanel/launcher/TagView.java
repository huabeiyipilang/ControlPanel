package cn.kli.controlpanel.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import cn.kli.controlpanel.Module;
import cn.kli.controlpanel.R;
import cn.kli.utils.UIUtils;


public class TagView extends BaseTagView {
	private Module mModule;

	public TagView(Context context, Module module) {
		super(context);
		mModule = module;
		bindApp(mModule);
	}
	
	/*
	private void init(){
		DisplayMetrics dm = new DisplayMetrics();   
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);  
		int size = dm.widthPixels/3;
		LayoutParams params = (LayoutParams) getLayoutParams();
		if(params == null){
			params = new LayoutParams(size, size);
		}
		this.setLayoutParams(params);
	}*/

	private void bindApp(Module module){
		setTagName(getContext().getString(module.name));
		setTagIcon(module.icon);
	}
	
	@Override
	public void onClick(){
		launchApp(mModule);
	}
	
	private void launchApp(Module module){
		Intent intent = new Intent(getContext(), module.cls);
		intent.putExtra("module_name", module.name);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getContext().startActivity(intent);
	}

	@Override
	protected void onLongClick() {
		super.onLongClick();
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage(R.string.create_icon_on_launcher);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				UIUtils.addShortcut(getContext(), new Intent(getContext(),mModule.cls), mModule.name, mModule.icon);
			}
		});
		builder.create().show();
	}
	
	
}
