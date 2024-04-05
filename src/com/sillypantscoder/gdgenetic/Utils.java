package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.function.Function;

public class Utils {
	public static<T> ArrayList<T> makeAL(T[] list) {
		ArrayList<T> result = new ArrayList<T>();
		for (int i = 0; i < list.length; i++) {
			result.add(list[i]);
		}
		return result;
	}
	public static<T,E> ArrayList<E> map(ArrayList<T> list, Function<T,E> converter) {
		ArrayList<E> result = new ArrayList<E>();
		for (int i = 0; i < list.size(); i++) {
			result.add(converter.apply(list.get(i)));
		}
		return result;
	}
}
