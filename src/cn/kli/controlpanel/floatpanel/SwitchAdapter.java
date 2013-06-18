package cn.kli.controlpanel.floatpanel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.kli.controlwidgets.ASwitch;
import cn.kli.controlwidgets.WidgetFactory;

public class SwitchAdapter extends BaseAdapter {
	private Context mContext;
	private List<ASwitch> mSwitchList;
	
	public SwitchAdapter(Context context, int[] switches) {
		super();
		mContext = context;
		mSwitchList = new ArrayList<ASwitch>();
		for(int s:switches){
			mSwitchList.add((ASwitch)WidgetFactory.createControlbar(mContext, s));
		}
	}

	@Override
	public int getCount() {
		return mSwitchList.size();
	}

	@Override
	public ASwitch getItem(int location) {
		return mSwitchList.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		return getItem(location);
	}

}
