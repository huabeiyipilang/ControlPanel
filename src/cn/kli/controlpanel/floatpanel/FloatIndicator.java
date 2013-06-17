package cn.kli.controlpanel.floatpanel;

import android.content.Context;
import android.view.WindowManager;

public class FloatIndicator extends FloatView{

	public FloatIndicator(Context context, WindowManager winManager) {
		super(context, winManager);
	}

	@Override
	int onInflaterContentView() {
		return 0;
	}
	
}
