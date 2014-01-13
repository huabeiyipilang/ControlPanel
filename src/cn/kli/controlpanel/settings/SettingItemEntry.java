package cn.kli.controlpanel.settings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.TextView;
import cn.kli.controlpanel.R;

public class SettingItemEntry extends SettingItem{
	private TextView mTitle;
	private Class<?> mTarget;

	public SettingItemEntry(Context context, Class<?> cls) {
		super(context);
		mTarget = cls;
	}

    @Override
    void onViewInit() {
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
		getContext().startActivity(new Intent(getContext(), mTarget));
	}
	
}
