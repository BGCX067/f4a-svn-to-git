package org.f4a.ioc;

public class Injector implements InjectorIntf {

	@Override
	public void register(Class<?> c, Object o) {
		if (impl == null) { return; }
		impl.register(c, o);
	}

	@Override
	public Class<?> createClass(String className) {
		if (impl == null) { return null; }
		return impl.createClass(className);
	}

	@Override
	public Object createObject(Class<?> c) {
		if (impl == null) { return null; }
		return impl.createObject(c);
	}

	@Override
	public void recycle(Object o) {
		if (impl == null) { return; }
		impl.recycle(o);
	}

	@Override
	public void inject(Object result) {
		if (impl == null) { return; }
		impl.inject(result);		
	}

	private static Injector impl;
	
	public static void startUp(Injector impl) {
		Injector.impl = impl;		
	}
	
	public static Injector impl() {
		return impl;
	}
}
