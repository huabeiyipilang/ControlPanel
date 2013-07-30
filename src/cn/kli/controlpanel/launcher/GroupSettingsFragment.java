package cn.kli.controlpanel.launcher;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.module.floatpanel.SettingsActivity;

public class GroupSettingsFragment extends GroupBaseFragment {
	private ListView mListView;
	private SettingAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mListView = new ListView(getActivity());
		mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mAdapter = new SettingAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				mAdapter.getItem(pos).onItemClick();
			}
			
		});
		return mListView;
	}

	class SettingAdapter extends BaseAdapter{
		ArrayList<SettingItem> mItemList = new ArrayList<SettingItem>();
		
		public SettingAdapter() {
			super();
			SettingItem item = new SettingItemToggle(getActivity(), Prefs.PREF_GUIDE_SHOW, null);
			item.setTitle(R.string.show_tips);
			mItemList.add(item);
			
			item = new SettingItemEntry(getActivity(), SettingsActivity.class);
			item.setTitle(R.string.module_float_panel);
			mItemList.add(item);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItemList.size();
		}

		@Override
		public SettingItem getItem(int arg0) {
			// TODO Auto-generated method stub
			return mItemList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return getItem(arg0);
		}
		
	}
	
}