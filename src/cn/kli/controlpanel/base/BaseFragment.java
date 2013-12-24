package cn.kli.controlpanel.base;

import android.os.Bundle;
import cn.kli.utils.klilog;

public class BaseFragment extends cn.kli.menuui.BaseFragment {
	protected klilog log;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log = new klilog(this.getClass());
	}


}
