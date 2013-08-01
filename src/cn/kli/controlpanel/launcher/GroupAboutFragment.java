package cn.kli.controlpanel.launcher;

import cn.kli.controlpanel.Baidu;

import com.baidu.mobstat.StatService;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class GroupAboutFragment extends GroupBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StatService.onEvent(getActivity(), Baidu.EVENT_ABOUT, Baidu.EVENT_ABOUT);
		WebView view = new WebView(getActivity());
		view.loadUrl("file:///android_asset/html/about.html");
		return view;
	}
	
}