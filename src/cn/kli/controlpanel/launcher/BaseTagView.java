package cn.kli.controlpanel.launcher;

import cn.kli.controlpanel.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseTagView extends LinearLayout {
	private Context mContext;
	private ImageView mIcon;
	private TextView mName;

	public BaseTagView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View root = inflater.inflate(R.layout.launcher_tag_view, this);
		mName = (TextView)root.findViewById(R.id.tag_name);
		mIcon = (ImageView)root.findViewById(R.id.tag_icon);
	}
	
	public void setTagName(String name){
		mName.setText(name);
	}
	
	public void setTagIcon(int icon){
		mIcon.setImageResource(icon);
	}
	
	public abstract void onClick();

	protected void onLongClick(){
		
	}
}
