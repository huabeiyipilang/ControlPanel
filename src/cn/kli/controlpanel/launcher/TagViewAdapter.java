package cn.kli.controlpanel.launcher;

import java.util.ArrayList;
import java.util.List;

import cn.kli.controlpanel.Config;
import cn.kli.controlpanel.Module;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TagViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<Module> mAppList;

	
	public TagViewAdapter(Context context) {
		super();
		mContext = context;
		mAppList = Config.getModules();
	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public Object getItem(int location) {
		return mAppList.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		return new TagView(mContext, mAppList.get(location));
	}

}
