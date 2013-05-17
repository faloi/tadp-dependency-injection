package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.HashMap;

public class Contexto {
	
	private Map<Class<?>, Object> bindings = new HashMap<Class<?>, Object>();

	private static Contexto instance;

	public static Contexto getInstance() {
		if (instance == null)
			instance = new Contexto();
		
		return instance;
	}

	public void agregarBinding(Class<?> baseType, Class<?> concreteType) {
		Object instancia = this.instanciarObjeto(concreteType);
		
		//Validar si ya lo tiene
		this.bindings.put(baseType, concreteType);
	}

	public Object obtenerInstancia(Class<?> baseType) {
		//Validar si no lo tiene
		//this.bindings.get(baseType);

		return null;
	}
	
	public Object instanciarObjeto(Class<?> clazz) {
		Constructor[] constructores = clazz.getConstructors();
		//if (constructores.length != 1) explotar();
		
		/*
		 * Invocar al unico constructor...
		 * Si tiene parámetros, invocar antes por cada uno a this.obtenerInstancia(tipo)
		 * 
		 * Esto facilitaría un montón todo, pero tiene la contra de tener que bindear en orden
		 * (no se si en los frameworks esto ya es así), pero si hay un:
		 * - Perro
		 * - CorreaParaPerro, que en el constructor recibe un perro
		 * Si trataras de bindear primero CorreaParaPerro rompería al no tener la instancia de Perro
		 */
		
		return null;
	}

	public void agregarBindingPrimitivo(Class<?> baseType, String string) {
		// TODO Auto-generated method stub
	}

}
