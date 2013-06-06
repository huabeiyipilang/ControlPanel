package cn.kli.controlpanel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ControlActivity extends FragmentActivity implements OnClickListener, OnItemClickListener {
	private ListView mMenuView;
	private ViewGroup mMainContainer;
	private DrawerLayout mDrawLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initUI();
	}
	
	private void initUI() {
		mMenuView = (ListView) findViewById(R.id.lv_menu_list);
		mMainContainer = (ViewGroup) findViewById(R.id.fl_main);
		mDrawLayout = (DrawerLayout)findViewById(R.id.dl_main);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				new String[] { "a", "b", "c" });
		mMenuView.setAdapter(adapter);
		mMenuView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mDrawLayout.setDrawerListener( 
                new DrawerLayout.SimpleDrawerListener()
            {
                @Override
                public void onDrawerClosed(View drawerView)
                {
                    super.onDrawerClosed(drawerView);
                    translateToFragment("cn.kli.controlpanel.ModulesFragment");
                }
            });
		mDrawLayout.closeDrawer(mMenuView);
	}

	private void translateToFragment(String to) {
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.fl_main, Fragment.instantiate(this, to));
		tx.commit();
	}
	
}
