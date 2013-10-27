package cn.kli.controlwidgets;

import cn.kli.controlpanel.R;
import cn.kli.utils.DeviceUtils;
import cn.kli.utils.klilog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.AttributeSet;

public class WidgetSwitchMobileNetwork extends ASwitch {

	public WidgetSwitchMobileNetwork(Context context) {
		super(context);
		init();
	}

	public WidgetSwitchMobileNetwork(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		setIcon(isConnected() ? R.drawable.light_on : R.drawable.light_off);
		setText(R.string.mobile_data);
	}

	@Override
	public void onClick() {
		boolean newState = !isConnected();
		setNetWorkEnable(newState);
	}

	private boolean isConnected() {
		return ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_MOBILE);
	}

	private int setNetWorkEnable(boolean enable) {
		int res = DeviceUtils.setMobileDataEnabled(getContext(), enable);
		klilog.info("switch mobile network to "+enable+", result:"+res);
		return res;
	}
}
