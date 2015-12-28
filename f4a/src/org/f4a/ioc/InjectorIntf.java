package org.f4a.ioc;

public interface InjectorIntf {

	void register(Class<?> c, Object o);

	Class<?> createClass(String className);

	Object createObject(Class<?> c);

	void recycle(Object o);

	void inject(Object result);
	
}