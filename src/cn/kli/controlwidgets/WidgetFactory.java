package cn.kli.controlwidgets;

import android.content.Context;

public class WidgetFactory {
	
	//control bars
	public final static int TYPE_MUSIC_VOLUME = 1;
	public final static int TYPE_RING_VOLUME = 2;
	public final static int TYPE_ALARM_VOLUME = 3;
	public final static int TYPE_SCREEN_BRIGHTNESS = 4;
	
	//control switches
	public final static int TYPE_SWITCH_MOBILE_DATA = 10;
	public final static int TYPE_SWITCH_WIFI_DATA = 11;
	
	public static IWidget createControlbar(Context context,int type){
		switch(type){
		case TYPE_MUSIC_VOLUME:
			return new MusicControlBarWidget(context);
		case TYPE_RING_VOLUME:
			return new RingControlBarWidget(context);
		case TYPE_ALARM_VOLUME:
			return new AlarmControlBarWidget(context);
		case TYPE_SCREEN_BRIGHTNESS:
			return new ScreenBrightnessWidget(context);
		case TYPE_SWITCH_MOBILE_DATA:
			return new WidgetSwitchMobileNetwork(context);
		case TYPE_SWITCH_WIFI_DATA:
		}
		return null;

	}
}
