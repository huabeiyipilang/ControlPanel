package cn.kli.controlpanel.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import cn.kli.controlpanel.Baidu;
import cn.kli.menuui.BaseFragment;

import com.baidu.mobstat.StatService;

public class AboutFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StatService.onEvent(getActivity(), Baidu.EVENT_ABOUT, Baidu.EVENT_ABOUT);
		WebView view = new WebView(getActivity());
		view.loadUrl("file:///android_asset/html/about.html");
		return view;
	}
	
}