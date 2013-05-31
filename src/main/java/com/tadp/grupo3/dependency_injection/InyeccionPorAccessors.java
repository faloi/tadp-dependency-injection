package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InyeccionPorAccessors extends EstrategiaInyeccion {
	
	public <T> T obtenerObjeto(Class<T> claseAInstanciar) {
		try {
			//(tiene que tener un constructor vacío)
			Object elObjeto = this.getContexto()
				.obtenerTipoPostaPara(claseAInstanciar)
				.newInstance();
			
			this.inyectarAccessors(elObjeto);
			
			return (T) elObjeto;
		} catch (InstantiationException e) {
			throw new RuntimeException();
		} catch (IllegalAccessException e) {
			throw new RuntimeException();
		}
	}
	
	private void inyectarAccessors(Object objeto) {
		Class<?> clase = objeto.getClass();
		
		Method[] metodos = clase.getMethods();
		
		//filter
		List<Method> settersAInyectar = new ArrayList<Method>();
		for(Method metodo : metodos) {
			if (metodo.getAnnotation(Inyectar.class) != null && metodo.getName().startsWith("set"))
				settersAInyectar.add(metodo);
		}
		
		for(Method setter : settersAInyectar) {
			try {
				Class<?> tipo = setter.getParameterTypes()[0];
				setter.invoke(objeto, this.obtenerObjeto(tipo));
			} catch (SecurityException e) {
				throw new RuntimeException();
			} catch (IllegalArgumentException e) {
				throw new RuntimeException();
			} catch (IllegalAccessException e) {
				throw new RuntimeException();
			} catch (InvocationTargetException e) {
				throw new RuntimeException();
			}
		}
	}
}