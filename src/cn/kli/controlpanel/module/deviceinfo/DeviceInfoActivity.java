package cn.kli.controlpanel.module.deviceinfo;

import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import cn.kli.controlpanel.R;

public class DeviceInfoActivity extends Activity {
	private static final int MSG_INIT_FINISH = 1;
	private ExpandableListView mInfoList;
	private List<InfoGroup> mGroups;
	private DeviceInfoManager mInfoManager;
	private LayoutInflater mInflater;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_INIT_FINISH:
				mInfoList.setAdapter(new InfoAdapter());
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_info);
		mInfoList = (ExpandableListView)findViewById(R.id.elv_device_info);
		mInfoManager = new DeviceInfoManager(this);
		mInflater = getLayoutInflater();
		new Thread(){

			@Override
			public void run() {
				super.run();
				mGroups = mInfoManager.getGroupList();
				mHandler.sendEmptyMessage(MSG_INIT_FINISH);
			}
			
		}.start();
	}

	private class InfoAdapter implements ExpandableListAdapter{

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public InfoChild getChild(int gpos, int cpos) {
			return mGroups.get(gpos).children.get(cpos);
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			return arg1;
		}

		@Override
		public View getChildView(int gpos, int cpos, boolean arg2, View convertView,
				ViewGroup arg4) {
			TextView tv = new TextView(DeviceInfoActivity.this);
			tv.setText(getChild(gpos, cpos).name);
			return tv;
		}

		@Override
		public int getChildrenCount(int gpos) {
			return mGroups.get(gpos).children.size();
		}

		@Override
		public long getCombinedChildId(long arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getCombinedGroupId(long arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public InfoGroup getGroup(int pos) {
			return mGroups.get(pos);
		}

		@Override
		public int getGroupCount() {
			return mGroups.size();
		}

		@Override
		public long getGroupId(int arg0) {
			return arg0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View convertView,
				ViewGroup arg3) {
			GroupHolder holder = null;
			if(convertView == null){
				holder = new GroupHolder();
				View root = mInflater.inflate(R.layout.module_deviceinfo_group_item, null);
				holder.title = (TextView)root.findViewById(R.id.tv_deviceinfo_group_title);
				convertView = root;
			}else{
				
			}
			TextView tv = new TextView(DeviceInfoActivity.this);
			tv.setText(getGroup(arg0).name);
			return tv;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public void onGroupCollapsed(int arg0) {
			
		}

		@Override
		public void onGroupExpanded(int arg0) {
			
		}

		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			
		}
		
		class GroupHolder{
			TextView title;
		}
		
		class ChildHolder{
			TextView title;
			TextView context;
		}
		
	}
}
