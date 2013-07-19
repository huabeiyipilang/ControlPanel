package cn.kli.controlpanel.launcher;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TagViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<Module> mModuleList;
	
	public TagViewAdapter(Context context, Group group) {
		super();
		mContext = context;
		mModuleList = group.getModuleList();
	}

	@Override
	public int getCount() {
		return mModuleList.size();
	}

	@Override
	public Object getItem(int location) {
		return mModuleList.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		return new TagView(mContext, mModuleList.get(location));
	}

	public void move(Object item, int from, int to) {
		if (from > to) {
			for (int i = from; i > to; i--) {
				mModuleList.set(i, mModuleList.get(i - 1));
			}
			mModuleList.set(to, (Module) item);
		} else {
			for (int i = from + 1; i <= to; i++) {
				mModuleList.set(i - 1, mModuleList.get(i));
			}
			mModuleList.set(to, (Module) item);
		}
		
		notifyDataSetChanged();
	}
}
