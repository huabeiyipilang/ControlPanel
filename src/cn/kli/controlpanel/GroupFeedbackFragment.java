package cn.kli.controlpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kli.controlpanel.launcher.TagGridView;

public class GroupFeedbackFragment extends GroupBaseFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TagGridView root = new TagGridView(getActivity());
		root.setGroup(Config.getGroupByClassName(this.getClass().getName()));
		return root;
	}
}