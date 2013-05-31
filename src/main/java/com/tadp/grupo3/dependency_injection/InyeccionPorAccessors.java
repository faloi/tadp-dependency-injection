package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InyeccionPorAccessors extends EstrategiaInyeccion {
	
	@Override
	public <T> T obtenerObjetoDesdeBindingDeClase(Class<T> claseAInstanciar, Class<?> solicitante) {
		try {
			//(tiene que tener un constructor vacío)
			Object elObjeto = this.getContexto()
				.obtenerTipoPostaPara(claseAInstanciar)
				.newInstance();
			
			this.inyectarAccessors(elObjeto, solicitante);
			
			return (T) elObjeto;
		} catch (InstantiationException e) {
			throw new RuntimeException();
		} catch (IllegalAccessException e) {
			throw new RuntimeException();
		}
	}
	
	private void inyectarAccessors(Object objeto, Class<?> solicitante) {
		Class<?> clase = objeto.getClass();
		
		Method[] metodos = clase.getMethods();
		
		//filter
		List<Method> settersAInyectar = new ArrayList<Method>();
		for(Method unMetodo : metodos) {
			if (this.puedoInyectar(unMetodo))
				settersAInyectar.add(unMetodo);
		}
		
		for(Method setter : settersAInyectar) {
			try {
				Class<?> tipo = setter.getParameterTypes()[0];
				setter.invoke(objeto, this.getContexto().obtenerObjeto(tipo, solicitante));
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

	private boolean puedoInyectar(Method unMetodo) {
		return unMetodo.isAnnotationPresent(Inyectar.class) && this.esUnSetter(unMetodo);
	}

	private boolean esUnSetter(Method unMetodo) {
		return unMetodo.getName().startsWith("set");
	}

}