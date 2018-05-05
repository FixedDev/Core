package us.sparknetwork.util;

import java.util.Map;

public class MapUtils {

	public static Integer getNoEntryValueIntMap() {
		return null;
	}

	public static Long getNoEntryValueLongMap() {
		return null;
	}

	public static <T> int AdjustOrPutValueIntMap(Map<T, Integer> map, T key, int Putvalue, int Adjustvalue) {
		if (map.containsKey(key)) {
			int oldvalue = map.get(key);
			int newvalue = oldvalue + Adjustvalue;
			map.put(key, newvalue);
			return newvalue;
		}
		map.put(key, Putvalue);
		return Putvalue;
	}

	public static <T> Long AdjustOrPutValueLongMap(Map<T, Long> map, T key, long Putvalue, long Adjustvalue) {
		if (map.containsKey(key)) {
			long oldvalue = map.get(key);
			long newvalue = oldvalue + Adjustvalue;
			map.put(key, newvalue);
			return newvalue;
		}
		map.put(key, Putvalue);
		return Putvalue;
	}

}
