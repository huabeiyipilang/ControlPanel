package cn.kli.controlpanel;

import com.actionbarsherlock.app.SherlockActivity;

import android.support.v4.app.Fragment;

public class GroupBaseFragment extends Fragment {

	@Override
	public void onResume() {
		super.onResume();
		((SherlockActivity) getActivity()).getSupportActionBar().setTitle(
				Config.getGroupByClassName(this.getClass().getName()).name);
	}

}
