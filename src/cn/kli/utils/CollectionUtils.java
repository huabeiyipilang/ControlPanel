package cn.kli.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {
	
	public static int[] integerListToIntArray(List<Integer> list) {
	    if (list == null) {
	        return null;
	    }
	    int[] result = new int[list.size()];
	    for (int i = 0; i < list.size(); ++i) {
	        result[i] = list.get(i).intValue();
	    }
	    return result;
	}
	
	public static int[] listToIntArray(List list) {
	    if (list == null) {
	        return null;
	    }
	    List<Integer> newList = new ArrayList<Integer>();
	    for (int i = 0; i < list.size(); ++i) {
	        newList.add(Integer.valueOf((String) list.get(i)));
	    }
	    return integerListToIntArray(newList);
	}
	
	public static int[] stringArray2IntArray(String[] array){
		List<Integer> newList = new ArrayList<Integer>();
		for(String item : array){
			try {
				newList.add(Integer.valueOf(item));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return integerListToIntArray(newList);
	}
}
