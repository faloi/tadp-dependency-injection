package com.tadp.grupo3.dependency_injection;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class InyeccionPorAccessors implements EstrategiaInyeccion {
	private Contexto contexto;
	
	public InyeccionPorAccessors(Contexto contexto) {
		this.contexto = contexto;
	}
	
	public <T> T obtenerObjeto(Class<T> claseAInstanciar) {
		try {
			//(tiene que tener un constructor vacío)
			Object elObjeto = contexto
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
		List<Method> metodosAInyectar = filter(having(on(Method.class).getAnnotation(Inyectar.class) != null), metodos);
		
		//sale café
	}
}