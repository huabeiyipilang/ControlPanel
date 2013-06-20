package cn.kli.controlpanel.floatpanel;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import cn.kli.controlpanel.Baidu;
import cn.kli.controlpanel.R;
import cn.kli.utils.klilog;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.baidu.mobstat.StatService;

public class ThemeSetting extends Activity implements OnSeekBarChangeListener, OnClickListener{
	
	public final static String SETTING_PREFERENCES = "setting_preferences";
	public final static String THEME_HEADER_COLOR = "theme_header_color";
	public final static String THEME_CONTAINER_COLOR = "theme_container_color";
	
	private final static int MODE_HEAD_SELECTED = 1;
	private final static int MODE_CONTAINER_SELECTED = 2;
	
	private int mSelectedMode = MODE_CONTAINER_SELECTED;
	private int mColorHeader;
	private int mColorContainer;
	
	private RelativeLayout mHeader;
	private LinearLayout mContainer;
	
	private SeekBar alphaSeekBar;
	private SeekBar redSeekBar;
	private SeekBar greenSeekBar;
	private SeekBar blueSeekBar;
	private TextView mNotice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sheme_setting);
		init();
	}
	
	
	private void init(){
		SharedPreferences share = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE);
		if(share == null){
			mColorHeader = getResources().getColor(R.color.translucent_background_dark);
			mColorContainer = getResources().getColor(R.color.translucent_background);
		}else{
			mColorHeader = share.getInt(THEME_HEADER_COLOR, 
					getResources().getColor(R.color.translucent_background_dark));
			mColorContainer = share.getInt(THEME_CONTAINER_COLOR, 
					getResources().getColor(R.color.translucent_background));
		}
		
		LinearLayout preview = (LinearLayout)findViewById(R.id.preview);
		preview.setBackgroundDrawable(getWallpaper());
		
		mHeader = (RelativeLayout)findViewById(R.id.header);
		mHeader.setOnClickListener(this);
		mContainer = (LinearLayout)findViewById(R.id.container);
		mContainer.setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		findViewById(R.id.reset).setOnClickListener(this);
		findViewById(R.id.swap).setOnClickListener(this);
		mNotice = (TextView)findViewById(R.id.notice);
		
		//get selected color
		updatePreview(true);
		
		//init ad view
		final LinearLayout adContainer = (LinearLayout)findViewById(R.id.ad_container);
		AdView adView = (AdView)findViewById(R.id.ad_view);
		adView.setListener(new AdViewListener(){

			@Override
			public void onAdClick(JSONObject arg0) {
				klilog.i("onAdClick");
				adContainer.setVisibility(View.GONE);
			}

			@Override
			public void onAdFailed(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAdReady(AdView arg0) {
				adContainer.setVisibility(View.VISIBLE);
				klilog.i("onAdReady");
			}

			@Override
			public void onAdShow(JSONObject arg0) {
				klilog.i("onAdShow");
			}

			@Override
			public void onAdSwitch() {
				klilog.i("onAdSwitch");
			}

			@Override
			public void onVideoFinish() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onVideoStart() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	private int getSelectedColor(){
		int color = 0;
		switch(mSelectedMode){
		case MODE_HEAD_SELECTED:
			color = mColorHeader;
			break;
		case MODE_CONTAINER_SELECTED:
			color = mColorContainer;
			break;
		}
		return color;
	}
	
	private void setSelectedColor(int color){
		switch(mSelectedMode){
		case MODE_HEAD_SELECTED:
			mColorHeader = color;
			break;
		case MODE_CONTAINER_SELECTED:
			mColorContainer = color;
			break;
		}
	}
	
	private String getAreaName(){
		switch(mSelectedMode){
		case MODE_HEAD_SELECTED:
			return (String) getResources().getText(R.string.setting_theme_area_title);
		case MODE_CONTAINER_SELECTED:
			return (String) getResources().getText(R.string.setting_theme_area_container);
		}
		return null;
	}
	
	private void initSeekBars(int color){
		alphaSeekBar = (SeekBar)findViewById(R.id.alpha_bar);
		alphaSeekBar.setMax(255);
		alphaSeekBar.setProgress(Color.alpha(color));
		alphaSeekBar.setOnSeekBarChangeListener(this);
		
		redSeekBar = (SeekBar)findViewById(R.id.red_bar);
		redSeekBar.setMax(255);
		redSeekBar.setProgress(Color.red(color));
		redSeekBar.setOnSeekBarChangeListener(this);
		
		greenSeekBar = (SeekBar)findViewById(R.id.green_bar);
		greenSeekBar.setMax(255);
		greenSeekBar.setProgress(Color.green(color));
		greenSeekBar.setOnSeekBarChangeListener(this);
		
		blueSeekBar = (SeekBar)findViewById(R.id.blue_bar);
		blueSeekBar.setMax(255);
		blueSeekBar.setProgress(Color.blue(color));
		blueSeekBar.setOnSeekBarChangeListener(this);
	}
	
	private void updatePreview(boolean updateBars){
		mNotice.setText(getResources().getString(R.string.setting_theme_select_area, getAreaName()));
		if(updateBars){
			initSeekBars(getSelectedColor());
		}
		mHeader.setBackgroundColor(mColorHeader);
		mContainer.setBackgroundColor(mColorContainer);
	}

	public void onProgressChanged(SeekBar bar, int progress, boolean user) {
		int selectColor = getSelectedColor();
		if (!user) {
			return;
		}
		switch (bar.getId()) {
		case R.id.alpha_bar:
			selectColor = Color.argb(progress, Color.red(selectColor), Color
					.green(selectColor), Color.blue(selectColor));
			break;
		case R.id.red_bar:
			selectColor = Color.argb(Color.alpha(selectColor), progress, Color
					.green(selectColor), Color.blue(selectColor));
			break;
		case R.id.green_bar:
			selectColor = Color.argb(Color.alpha(selectColor), Color
					.red(selectColor), progress, Color.blue(selectColor));
			break;
		case R.id.blue_bar:
			selectColor = Color.argb(Color.alpha(selectColor), Color
					.red(selectColor), Color.green(selectColor), progress);
			break;
		}
		setSelectedColor(selectColor);
		updatePreview(false);
	}

	public void onStartTrackingTouch(SeekBar arg0) {
		
	}

	public void onStopTrackingTouch(SeekBar arg0) {
		
	}
	
	private boolean save(){
		klilog.i("save");
		SharedPreferences share = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		editor.putInt(THEME_HEADER_COLOR, mColorHeader);
		editor.putInt(THEME_CONTAINER_COLOR, mColorContainer);
		editor.commit();
		StatService.onEvent(this, Baidu.EVENT_THEME_SETTINGS, Baidu.SET_THEME+"header: "+dumpColor(mColorHeader)+";   container: "+dumpColor(mColorContainer));
		return true;
	}
	
	private String dumpColor(int color){
		String log = "color = "+color+", alpha = "+Color.alpha(color)+","
				+Color.red(color)+","
				+Color.green(color)+","
				+Color.blue(color);
		klilog.i(log);
		return log;
	}

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.save:
			save();
			finish();
			break;
		case R.id.swap:
			if(mSelectedMode == MODE_HEAD_SELECTED){
				mSelectedMode = MODE_CONTAINER_SELECTED;
			}else{
				mSelectedMode = MODE_HEAD_SELECTED;
			}
			updatePreview(true);
			break;
		case R.id.reset:
			mColorHeader = getResources().getColor(R.color.translucent_background_dark);
			mColorContainer = getResources().getColor(R.color.translucent_background);
			save();
			updatePreview(true);
			break;
		case R.id.header:
			mSelectedMode = MODE_HEAD_SELECTED;
			updatePreview(true);
			break;
		case R.id.container:
			mSelectedMode = MODE_CONTAINER_SELECTED;
			updatePreview(true);
			break;
		}
	}
	

	@Override
	protected void onPause() {
		StatService.onPause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();
	}
	
}
