package cn.kli.controlpanel.guide;

import cn.kli.controlpanel.R;
import cn.kli.utils.klilog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TipsView extends LinearLayout {
	private final static int MSG_NEXT = 1;
	private String[] mStrings = null;
	private int index = 0;
	private TextView mDisplay;
	private String mCurrentTips;
	private Animation mSlideInAnim;
	private Animation mSlideOutAnim;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_NEXT:
				if(mStrings != null && mStrings.length > 0){
					if(index >= mStrings.length){
						index = 0;
					}
					mCurrentTips = mStrings[index++];
					klilog.i("show tips:"+mCurrentTips);
					changeTipsWithAnim();
					sendEmptyMessageDelayed(MSG_NEXT, 10000);
				}
				break;
			}
		}
		
	};
	
	private void changeTipsWithAnim(){
		mDisplay.startAnimation(mSlideOutAnim);
	}

	public TipsView(Context context) {
		super(context);
		init();
	}

	public TipsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.tips_view, this);
		mDisplay = (TextView)findViewById(R.id.tv_tips_show);
		mStrings = getContext().getResources().getStringArray(R.array.guide_tips);
		mSlideInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_up);
		mSlideInAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				klilog.i("Animation end");
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				klilog.i("Animation start");
			}
			
		});
		
		mSlideOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
		mSlideOutAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				mDisplay.setText(mCurrentTips);
				mDisplay.startAnimation(mSlideInAnim);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
		});
	}
	
	public void start(){
		mHandler.sendEmptyMessage(MSG_NEXT);
	}
	
	public void stop(){
		mHandler.removeMessages(MSG_NEXT);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		start();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stop();
	}
	
	
}
