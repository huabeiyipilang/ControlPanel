package cn.kli.utils;

import java.util.Calendar;

public class CalendarUtils {
	public static boolean isSameDay(Calendar a, Calendar b){
		boolean res = false;
		if(a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
				&& a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)){
			res = true;
		}
		return res;
	}
}
