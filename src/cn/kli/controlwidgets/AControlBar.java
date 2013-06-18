package cn.kli.controlwidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.kli.controlpanel.R;
import cn.kli.utils.klilog;

public abstract class AControlBar extends LinearLayout implements IWidget{
	private ImageView mIcon;
	private SeekBar mBar;
	private TextView mDescription;
	
	public AControlBar(Context context){
		super(context);
		initViews();
	}
	
	public AControlBar(Context context, AttributeSet attrs){
		super(context,attrs);
		initViews();
	}
	
	private void initViews(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
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
		klilog.i("current = "+current);
		mBar.setProgress(current);
	}
	
	protected void setDescription(int res){
		mDescription.setText(res);
	}
	
	private class SeekBarListener implements OnSeekBarChangeListener{
		private int lastProgress;
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			klilog.i("onProgressChanged  fromUser = "+fromUser);
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
