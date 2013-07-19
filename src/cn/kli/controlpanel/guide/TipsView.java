package cn.kli.controlpanel.guide;

import cn.kli.controlpanel.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TipsView extends LinearLayout {
	private final static int MSG_NEXT = 1;
	private String[] mStrings = null;
	private int index = 0;
	private TextView mDisplay;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_NEXT:
				if(mStrings != null && mStrings.length > 0){
					if(index > mStrings.length){
						index = 0;
					}
					mDisplay.setText(mStrings[index++]);
					sendEmptyMessageDelayed(MSG_NEXT, 5000);
				}
				break;
			}
		}
		
	};

	public TipsView(Context context) {
		super(context);
		
	}

	public TipsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public void start(){
		mStrings = getContext().getResources().getStringArray(R.array.guide_tips);
		mDisplay = new TextView(getContext()); 
		mDisplay.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		mDisplay.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		mHandler.sendEmptyMessage(MSG_NEXT);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	
	
}
