package net.scholagest.app.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JerseyHelper {
	public static <T, U> Map<T, U> listToMap(List<T> keys, List<U> values) {
		Map<T, U> result = new HashMap<T, U>();
		
		for (int i = 0; i < keys.size(); i++) {
			result.put(keys.get(i), values.get(i));
		}
		
		return result;
	}
}
