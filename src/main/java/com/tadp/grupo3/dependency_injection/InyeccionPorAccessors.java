package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.NoTieneConstructorVacioException;

public class InyeccionPorAccessors extends EstrategiaInyeccion {
	
	@Override
	public Object obtenerObjeto(Class<?> tipoPosta, Class<?> solicitante) {
		this.validarQuePuedoInstanciar(tipoPosta);
		return this.crearEInyectar(tipoPosta, solicitante);
	}
	
	private void validarQuePuedoInstanciar(Class<?> unaClase) {
		try {
			unaClase.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new NoTieneConstructorVacioException();
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Object crearEInyectar(Class<?> tipoPosta, Class<?> solicitante) {
		try {
			Object objeto = tipoPosta.newInstance();
			Class<?> clase = objeto.getClass();
			
			Method[] metodos = clase.getMethods();
			
			//filter
			List<Method> settersAInyectar = new ArrayList<Method>();
			for(Method unMetodo : metodos) {
				if (this.puedoInyectar(unMetodo))
					settersAInyectar.add(unMetodo);
			}
			
			for(Method setter : settersAInyectar) {
				Class<?> tipo = setter.getParameterTypes()[0];
				setter.invoke(objeto, this.getContexto().obtenerObjeto(tipo, solicitante));
			}
			
			return objeto;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean puedoInyectar(Method unMetodo) {
		return unMetodo.isAnnotationPresent(Inyectar.class) && this.esUnSetter(unMetodo);
	}

	private boolean esUnSetter(Method unMetodo) {
		return unMetodo.getName().startsWith("set");
	}

}