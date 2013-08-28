/**
 *  Author :  hmg25
 *  Description :
 */
package cn.kli.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Conversion {
	
	public static Bitmap drawable2Bitmap(Drawable d){
		BitmapDrawable bd = (BitmapDrawable) d;
		return bd.getBitmap();
	}
}
