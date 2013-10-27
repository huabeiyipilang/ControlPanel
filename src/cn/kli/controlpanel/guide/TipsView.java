package cn.kli.controlpanel.guide;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.utils.CollectionUtils;
import cn.kli.utils.klilog;

public class TipsView extends LinearLayout implements View.OnClickListener {
	private final static int MSG_NEXT = 1;
	private String[] mStrings = null;
	private TextView mDisplay;
	private String mCurrentTips;
	private ImageView mClose;
	private Animation mSlideInAnim;
	private Animation mSlideOutAnim;
	private Random random = new Random();
	private int index = 0;
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
					mCurrentTips = mStrings[index];
					index++;
					klilog.info("show tips:"+mCurrentTips);
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
		mClose = (ImageView)findViewById(R.id.iv_tips_close);
		mClose.setOnClickListener(this);
		mStrings = getContext().getResources().getStringArray(R.array.guide_tips);
		mStrings = CollectionUtils.randomSort(mStrings);
		mSlideInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_up);
		mSlideInAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				klilog.info("Animation end");
				mDisplay.requestFocus();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				klilog.info("Animation start");
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
		SharedPreferences prefs = Prefs.getPrefs(getContext());
		boolean show = prefs.getBoolean(Prefs.PREF_GUIDE_SHOW, true);
		this.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
	public void start(){
		klilog.info("start tips");
		mHandler.sendEmptyMessage(MSG_NEXT);
	}
	
	public void stop(){
		klilog.info("stop tips");
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

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iv_tips_close:
			this.setVisibility(View.GONE);
			SharedPreferences prefs = Prefs.getPrefs(getContext());
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(Prefs.PREF_GUIDE_SHOW, false);
			editor.commit();
			break;
		}
	}
	
	
}
