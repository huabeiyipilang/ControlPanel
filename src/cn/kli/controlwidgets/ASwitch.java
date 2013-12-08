package cn.kli.controlwidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kli.controlpanel.R;

public abstract class ASwitch extends LinearLayout implements IWidget {
	public static final int STAT_OFF = 0;
	public static final int STAT_ON = 1;
	public static final int STAT_OFFING = 2;
	public static final int STAT_ONING = 3;
	
	private View mRoot;
	private ImageView mIcon;
	private TextView mTitle;

	public ASwitch(Context context) {
		super(context);
		initViews();
	}

	public ASwitch(Context context, AttributeSet attrs){
		super(context,attrs);
		initViews();
	}

	
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		mRoot = inflater.inflate(R.layout.switch_widget,this, true);
		mIcon = (ImageView)mRoot.findViewById(R.id.tv_switch_icon);
		mTitle = (TextView)mRoot.findViewById(R.id.tv_switch_title);
	}
	
	public void setIcon(int icon){
		mIcon.setImageResource(icon);
	}
	
	public void setText(int title){
		mTitle.setText(title);
	}
	
	public abstract void onClick();
}
