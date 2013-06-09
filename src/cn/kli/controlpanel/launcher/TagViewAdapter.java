package cn.kli.controlpanel.launcher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.kli.controlpanel.Group;

public class TagViewAdapter extends BaseAdapter {
	private Context mContext;
	private Group mGroup;

	
	public TagViewAdapter(Context context, Group group) {
		super();
		mContext = context;
		mGroup = group;
	}

	@Override
	public int getCount() {
		return mGroup.getModuleList().size();
	}

	@Override
	public Object getItem(int location) {
		return mGroup.getModuleList().get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		return new TagView(mContext, mGroup.getModuleList().get(location));
	}

}
