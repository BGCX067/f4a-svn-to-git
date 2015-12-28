package org.f4a.annotation.processing.json;

public interface JSONBinder <T> {
	public String toJSON(T o);
	public T fromJSON(String json);
}
