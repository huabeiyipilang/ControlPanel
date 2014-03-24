package cn.kli.controlpanel.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import cn.kli.controlpanel.base.BaseFragment;

public class AboutFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		WebView view = new WebView(getActivity());
		view.loadUrl("file:///android_asset/html/about.html");
		return view;
	}
	
}