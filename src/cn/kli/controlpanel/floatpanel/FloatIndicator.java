package cn.kli.controlpanel.floatpanel;

import cn.kli.controlpanel.R;
import cn.kli.utils.klilog;
import android.content.Context;
import android.view.WindowManager;

public class FloatIndicator extends FloatView{

	private int mScreenWidth;
	
	public FloatIndicator(Context context, WindowManager winManager) {
		super(context, winManager);
		mScreenWidth = 540;
	}

	@Override
	int onInflaterContentView() {
		return R.layout.float_indicator;
	}

	@Override
	protected void onActionUp(float x, float y, int originX, int originY) {
		x = x + originX > 0 ? mScreenWidth / 2 : - mScreenWidth / 2;
		originX = 0;
		super.onActionUp(x, y, originX, originY);
	}
	
}
