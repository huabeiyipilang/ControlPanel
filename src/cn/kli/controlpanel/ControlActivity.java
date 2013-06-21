package cn.kli.controlpanel;

import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ControlActivity extends SherlockActivity  implements OnClickListener, OnItemClickListener {
	private ListView mMenuView;
	private DrawerLayout mDrawLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initUI();
		translateToFragment(Config.getGroupList().get(0).cls
				.getName());
	}
	
	private void initUI() {
		mMenuView = (ListView) findViewById(R.id.lv_menu_list);
		mDrawLayout = (DrawerLayout)findViewById(R.id.dl_main);
//		mDrawLayout.setBackgroundDrawable(getWallpaper());
		mDrawLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
			
			
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setDisplayHomeAsUpEnabled(false); 
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
			}
		});
		mMenuView.setAdapter(new GroupListAdapter());
		mMenuView.setOnItemClickListener(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			mDrawLayout.openDrawer(mMenuView);
			break;
		}
		return true;
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int pos,
			long arg3) {
		mDrawLayout.closeDrawer(mMenuView);
		translateToFragment(Config.getGroupList().get(pos).cls
				.getName());
	}

	private void translateToFragment(final String to) {
		new Thread(){
			@Override
			public void run() {
				super.run();
				FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
				tx.replace(R.id.fl_main, Fragment.instantiate(ControlActivity.this, to));
				tx.commit();
			}
		}.start();
	}
	
	private class GroupListAdapter extends BaseAdapter{
		
		List<Group> groups = Config.getGroupList();
		
		@Override
		public int getCount() {
			return groups.size();
		}

		@Override
		public Object getItem(int arg0) {
			return groups.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if(convertView == null){
				LayoutInflater inflater = ControlActivity.this.getLayoutInflater();
				convertView = inflater.inflate(R.layout.group_list_item, null);
				holder = new ViewHolder();
				holder.title = (TextView)convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.title.setText(groups.get(pos).name);
			return convertView;
		}
		
		class ViewHolder{
			TextView title;
		}
	}
	
}
