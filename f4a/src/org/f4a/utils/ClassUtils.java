package org.f4a.utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ClassUtils {
	private static Set<Class> SIMPLE_CLASSES = createSimpleClassesSet();  

	public static boolean isSimpleClass(Class c) {
		return SIMPLE_CLASSES.contains(c);
	}

	private static Set<Class> createSimpleClassesSet() {
		Set<Class> result = new HashSet<Class>();
		result.add(Integer.class);
		result.add(String.class);
		result.add(Date.class);
		result.add(Double.class);
		return result;
	}
}
