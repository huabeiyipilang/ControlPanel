package cn.kli.controlpanel;

import cn.kli.controlpanel.launcher.TagGridView;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupBaseFragment extends Fragment {

	@Override
	public void onResume() {
		super.onResume();
		((SherlockActivity) getActivity()).getSupportActionBar().setTitle(
				Config.getGroupByClassName(this.getClass().getName()).name);
	}
	
}
