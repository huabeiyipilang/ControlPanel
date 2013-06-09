package cn.kli.controlpanel.module.deviceinfo;

import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import cn.kli.controlpanel.R;

public class DeviceInfoActivity extends Activity {
	private static final int MSG_INIT_FINISH = 1;
	private ExpandableListView mInfoList;
	private List<InfoGroup> mGroups;
	private DeviceInfoManager mInfoManager;
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
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
				ViewGroup arg4) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return 0;
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
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onGroupCollapsed(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGroupExpanded(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
