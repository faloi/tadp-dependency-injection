package com.tadp.grupo3.dependency_injection;
import java.lang.reflect.Constructor;

public class PorConstructorStrategy implements ConstruccionStrategy {

	public <T> T instanciar(Class<T> unaClase, Contexto unContexto) {
		 Constructor[] unosConstructores = unaClase.getDeclaredConstructors();
		 
	}
}
