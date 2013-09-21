package cn.kli.controlpanel.launcher;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.R.id;
import cn.kli.controlpanel.R.layout;
import cn.kli.utils.UIUtils;
import cn.kli.utils.klilog;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class ControlActivity extends SherlockActivity  implements OnClickListener, OnItemClickListener {
	private ListView mMenuView;
	private DrawerLayout mDrawLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initUI();
		initData();
		translateToFragment(Config.getGroupList().get(0).cls
				.getName());
	}
	
	private void initData(){
//		if(Prefs.getPrefs(this).getInt(Prefs.PREF_STATUSBAR_HEIGHT, -1) != -1){
//			return;
//		}
		int statusbarHeight = UIUtils.getStatusHeight(this);
		klilog.i("statusbarHeight:"+statusbarHeight);
		Editor editor = Prefs.getPrefs(this).edit();
		editor.putInt(Prefs.PREF_STATUSBAR_HEIGHT, statusbarHeight);
		editor.commit();
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

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!mDrawLayout.isDrawerOpen(mMenuView)){
				mDrawLayout.openDrawer(mMenuView);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
