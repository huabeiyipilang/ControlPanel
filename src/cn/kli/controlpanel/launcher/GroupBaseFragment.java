package cn.kli.controlpanel.launcher;


import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class GroupBaseFragment extends Fragment {

	@Override
	public void onResume() {
		super.onResume();
		((SherlockFragmentActivity) getActivity()).getSupportActionBar().setTitle(
				Config.getGroupByClassName(this.getClass().getName()).name);
	}
	
}
