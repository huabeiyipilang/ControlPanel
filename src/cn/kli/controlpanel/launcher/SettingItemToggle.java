package cn.kli.controlpanel.launcher;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;

public class SettingItemToggle extends SettingItem implements OnCheckedChangeListener{
	private TextView mTitle;
	private CheckBox mEnable;
	private String mPrefKey;
	private ChangeListener mListener;
	
	public interface ChangeListener{
		void onCheckedChanged(boolean enable);
	}

	public SettingItemToggle(Context context, String key, ChangeListener listener) {
		super(context);
		mPrefKey = key;
		mListener = listener;
		init();
	}
	
	private void init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.settings_item_toggle, this);
		mTitle = (TextView)findViewById(R.id.tv_title);
		mEnable = (CheckBox)findViewById(R.id.cb_enable);
		boolean enable = Prefs.getPrefs(getContext()).getBoolean(mPrefKey, false);
		mEnable.setChecked(enable);
		mEnable.setOnCheckedChangeListener(this);
	}

	public void setTitle(int res){
		mTitle.setText(res);
	}

	public void setTitle(String res){
		mTitle.setText(res);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		Editor editor = Prefs.getPrefs(getContext()).edit();
		editor.putBoolean(mPrefKey, arg1);
		editor.commit();
		if(mListener != null){
			mListener.onCheckedChanged(arg1);
		}
	}

	@Override
	protected void onItemClick() {
		super.onItemClick();
		mEnable.toggle();
	}
	
	
}
