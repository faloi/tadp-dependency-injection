package com.tadp.grupo3.dependency_injection.converters;

import ch.lambdaj.function.convert.Converter;

public class ObjectToClassConverter implements Converter<Object, Class<?>> {
	public Class<?> convert(Object from) {
		return from.getClass();
	}
}
