package com.tadp.grupo3.dependency_injection;

public class Contexto {

	private static Contexto instance;

	public static Contexto getInstance() {
		if (instance == null)
			instance = new Contexto();
		
		return instance;
	}

	public void agregarBinding(Class<?> baseType, Class<?> concreteType) {
		// TODO Auto-generated method stub
	}

	public Object obtenerInstancia(Class<?> baseType) {
			// TODO Auto-generated method stub
			return null;
	}

	public void agregarBindingPrimitivo(Class<?> baseType, String string) {
		// TODO Auto-generated method stub
		
	}

}
