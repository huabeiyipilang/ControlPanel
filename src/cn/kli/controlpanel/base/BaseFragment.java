package cn.kli.controlpanel.base;

import android.os.Bundle;
import cn.kli.utils.klilog;

import com.actionbarsherlock.app.SherlockFragment;

public class BaseFragment extends SherlockFragment {
	protected klilog log;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log = new klilog(this.getClass());
	}


}
