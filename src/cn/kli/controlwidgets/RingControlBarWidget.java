package cn.kli.controlwidgets;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import cn.kli.controlpanel.R;

public class RingControlBarWidget extends ControlBar implements IWidget {
	private AudioManager am;
	private final static int TYPE = AudioManager.STREAM_RING;
	private final static int TYPE2 = AudioManager.STREAM_NOTIFICATION;

	public RingControlBarWidget(Context context){
		super(context);
		init(context);
	}
	
	public RingControlBarWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		setIcon(R.drawable.ic_audio_ring_notif);
		setBar(am.getStreamMaxVolume(TYPE), am.getStreamVolume(TYPE));
		setDescription(R.string.description_ring_notify);
	}

	@Override
	void updateValue(int value) {
		am.setStreamVolume(TYPE, value, 0);
		am.setStreamVolume(TYPE2, value, 0);
	}

}
