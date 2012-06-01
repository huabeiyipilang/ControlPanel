package cn.kli.controlwidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import cn.kli.controlpanel.KLog;
import cn.kli.controlpanel.R;

public abstract class ControlBar extends LinearLayout{
	protected Context mContext;
	private ImageView mIcon;
	private SeekBar mBar;
	private TextView mDescription;
	
	public ControlBar(Context context){
		super(context);
		mContext = context;
		initViews();
	}
	
	public ControlBar(Context context, AttributeSet attrs){
		super(context,attrs);
		mContext = context;
		initViews();
	}
	
	private void initViews(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.control_bar,this, true);
		mIcon = (ImageView)findViewById(R.id.icon);
		mBar = (SeekBar)findViewById(R.id.bar);
		mDescription = (TextView)findViewById(R.id.descripiton);
		mBar.setOnSeekBarChangeListener(new SeekBarListener());
	}
	
	protected void setIcon(int res){
		mIcon.setBackgroundResource(res);
	}
	
	protected void setBar(int max, int current){
		mBar.setMax(max);
		KLog.i("current = "+current);
		mBar.setProgress(current);
	}
	
	protected void setDescription(int res){
		mDescription.setText(res);
	}
	
	private class SeekBarListener implements OnSeekBarChangeListener{
		private int lastProgress;
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			KLog.i("onProgressChanged  fromUser = "+fromUser);
			if(fromUser && lastProgress != progress){
				updateValue(progress);
				lastProgress = progress;
			}
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			
		}
		
	}
	
	abstract void updateValue(int value);
	
}
