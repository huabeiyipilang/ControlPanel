package cn.kli.controlwidgets;

import cn.kli.controlpanel.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class ACheckItem extends LinearLayout implements IWidget {
	private Context mContext;

	public ACheckItem(Context context) {
		super(context);
		mContext = context;
		initViews();
	}


	public ACheckItem(Context context, AttributeSet attrs){
		super(context,attrs);
		mContext = context;
		initViews();
	}

	
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.check_box_widget,this, true);
	}
	
}
