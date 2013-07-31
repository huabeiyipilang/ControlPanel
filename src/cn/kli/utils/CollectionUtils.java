package cn.kli.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	
	public static String[] randomSort(String[] array){
		Random random = new Random();
		String tmp;
		for(int i = 0; i < array.length; i++){
			int p = random.nextInt(array.length);
			tmp = array[i];
			array[i] = array[p];
			array[p] = tmp;
		}
		return array;
	}
}
