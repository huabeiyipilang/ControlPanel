package cn.kli.utils;

import android.util.Log;

public class klilog {
	private final static String TAG = "weather";
	
	public static void i(String msg){
		Log.i(TAG, msg);
	}
	
	public static void e(String msg){
		Log.e(TAG, msg);
	}
	
}
