package cn.kli.controlpanel.launcher;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupBaseFragment extends Fragment {

	@Override
	public void onResume() {
		super.onResume();
		((SherlockFragmentActivity) getActivity()).getSupportActionBar().setTitle(
				Config.getGroupByClassName(this.getClass().getName()).name);
	}
	
}
