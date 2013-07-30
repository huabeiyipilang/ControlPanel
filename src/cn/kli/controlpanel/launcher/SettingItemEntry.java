package cn.kli.controlpanel.launcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import cn.kli.controlpanel.R;

public class SettingItemEntry extends SettingItem{
	private TextView mTitle;

	public SettingItemEntry(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.settings_item_entry, this);
		mTitle = (TextView)findViewById(R.id.tv_title);
	}

	public void setTitle(int res){
		mTitle.setText(res);
	}

	public void setTitle(String res){
		mTitle.setText(res);
	}

	@Override
	protected void onItemClick() {
		super.onItemClick();
	}
	
}
