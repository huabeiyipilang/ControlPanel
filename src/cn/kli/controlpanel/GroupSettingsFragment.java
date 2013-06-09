package cn.kli.controlpanel;

import cn.kli.controlpanel.launcher.TagGridView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupSettingsFragment extends GroupBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return new TagGridView(getActivity());
	}
	
}