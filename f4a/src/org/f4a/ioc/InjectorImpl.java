package org.f4a.ioc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InjectorImpl implements InjectorIntf {
	private Map<Class<?>, Object> allInstances = new HashMap<Class<?>, Object>();
	private Map<String, Class<?>> allClasses = new HashMap<String, Class<?>>();
	private static Map<Class<?>, List<Object>> instancesPool = new HashMap<Class<?>, List<Object>>();

	@Override
	public void register(Class<?> c, Object o) {
		allInstances.put(c, o);
	}

	@Override
	public Class<?> createClass(String className) {
		Class<?> result = allClasses.get(className);
		if (result != null) {
			return result;
		}
		try {
			result = Class.forName(className);
			allClasses.put(className, result);
		} catch (Exception e) {
			// /
		}
		return result;
	}

	@Override
	public Object createObject(Class<?> c) {
		Object result = null;
		List<Object> instancePool = instancesPool.get(c);
		if (instancePool == null) {
			synchronized (instancesPool) {
				instancePool = new LinkedList<Object>();
				instancesPool.put(c, instancePool);
			}
		}
		LinkedList<Object> linkedList = (LinkedList<Object>) instancePool;
		synchronized (linkedList) {
			result = linkedList.removeLast();
		}
		if (result == null) {
			result = createRawObject(c);
		}
		return result;
	}

	public Object createRawObject(Class<?> c) {
		Object result = null;
		try {
			result = c.newInstance();
			// Injector.inject(result);
		} catch (Exception e) {
			//
		}
		return result;
	}

	@Override
	public void recycle(Object o) {
		if (o == null) { return; }
		Class<?> c = o.getClass();
		List<Object> instancePool = instancesPool.get(c);
		if (instancePool == null) {
			return;
		}
		LinkedList<Object> linkedList = (LinkedList<Object>) instancePool;
		resetObject(o);
		synchronized (linkedList) {
			linkedList.addLast(o);
		}
	}

	private void resetObject(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inject(Object result) {

		// TODO Auto-generated method stub

	}
}
