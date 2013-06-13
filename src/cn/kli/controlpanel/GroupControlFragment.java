package cn.kli.controlpanel;

import cn.kli.controlpanel.launcher.TagGridView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupControlFragment extends GroupBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TagGridView root = new TagGridView(getActivity());
		root.setGroup(Config.getGroupByClassName(this.getClass().getName()));
		return root;
	}

}
