package cn.kli.controlwidgets;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import cn.kli.controlpanel.R;

public class MusicControlBarWidget extends AControlBar implements IWidget {
	private AudioManager am;
	private final static int TYPE = AudioManager.STREAM_MUSIC;

	public MusicControlBarWidget(Context context){
		super(context);
		init(context);
	}
	
	public MusicControlBarWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		setIcon(R.drawable.ic_audio_vol);
		setBar(am.getStreamMaxVolume(TYPE), am.getStreamVolume(TYPE));
		setDescription(R.string.description_music);
	}

	@Override
	void updateValue(int value) {
		am.setStreamVolume(TYPE, value, 0);
	}

}
