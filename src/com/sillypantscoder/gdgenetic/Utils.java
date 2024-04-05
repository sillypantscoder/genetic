package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;

public class Utils {
	public static<T> ArrayList<T> makeAL(T[] list) {
		ArrayList<T> result = new ArrayList<T>();
		for (int i = 0; i < list.length; i++) {
			result.add(list[i]);
		}
		return result;
	}
}
