package com.tadp.grupo3.dependency_injection;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnConstructorValidoException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoHayConstructorValidoException;

public class InyeccionPorConstructor implements EstrategiaInyeccion {
	private Contexto contexto;
	
	public InyeccionPorConstructor(Contexto contexto) {
		this.contexto = contexto;
	}
	
	public <T> T obtenerObjeto(Class<T> claseAInstanciar) {
		return this.obtenerObjeto(claseAInstanciar, claseAInstanciar);
	}
	
	private <T> T obtenerObjeto(Class<T> claseAInstanciar, Class<?> solicitante) {
		try {
			return this.obtenerObjetoDesdeBindingEspecifico(solicitante, claseAInstanciar);
		} catch (Exception e) {
			try {
				return this.obtenerObjetoDesdeBindingDeInstancia(solicitante, claseAInstanciar);
			} catch (Exception e2) {
				return this.obtenerObjetoDesdeBindingDeClase(claseAInstanciar, solicitante);	
			}
		}
	}
	
	private <TipoInstancia> TipoInstancia obtenerObjetoDesdeBindingEspecifico(Class<?> solicitante, Class<TipoInstancia> _) {
		List<BindingEspecifico> bindings = filter(having(on(BindingEspecifico.class).esValidoPara(solicitante, _)), contexto.getBindingsEspecificos());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		Object[] parametrosDelConstructor = bindings.get(0).getParametrosDelConstructor();
		
		List<Class<?>> tipoDeParametros = new ArrayList<Class<?>>();
		for (Object parametro : parametrosDelConstructor) {
			tipoDeParametros.add(parametro.getClass());
		}
		
		Constructor<?>[] constructores = solicitante.getConstructors();
		for (Constructor<?> constructor : constructores) {
			if (Arrays.equals(constructor.getParameterTypes(), tipoDeParametros.toArray()))
				try {
					return (TipoInstancia) constructor.newInstance(parametrosDelConstructor);
				} catch (Exception e) {
					throw new RuntimeException();
				}
		}
		
		throw new RuntimeException();
	}
	
	private <TipoInstancia> TipoInstancia obtenerObjetoDesdeBindingDeInstancia(Class<?> scope, Class<TipoInstancia> tipoInstancia) {
		List<BindingDeInstancia> bindings = filter(having(on(BindingDeInstancia.class).esValidoPara(scope, tipoInstancia)), contexto.getBindingsDeInstancia());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return (TipoInstancia) bindings.get(0).getInstancia();
	}

	private <T> T obtenerObjetoDesdeBindingDeClase(Class<T> claseAInstanciar, Class<?> solicitante) {
		Class<?> clasePosta = contexto.obtenerTipoPostaPara(claseAInstanciar);
		
		Constructor<?>[] constructores = clasePosta.getConstructors();
		
		if (this.esDirectamenteInstanciable(clasePosta, constructores))
			try {
				return (T) clasePosta.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException();
			} catch (IllegalAccessException e) {
				throw new RuntimeException();
			}
			
		List<Constructor<?>> constructoresValidos = new ArrayList<Constructor<?>>();
		for (Constructor<?> constructor : constructores) {
			if (this.puedoUsarEsteConstructor(constructor, solicitante))
				constructoresValidos.add(constructor);
		}
		
		if (constructoresValidos.isEmpty())
			throw new NoHayConstructorValidoException();
		
		if (constructoresValidos.size() > 1)
			throw new MasDeUnConstructorValidoException();
		
		return (T) this.instanciarObjetoUsando(constructoresValidos.get(0), solicitante);
	}

	private <T> boolean esDirectamenteInstanciable(Class<T> claseAInstanciar, Constructor<?>[] constructores) {
		Class<?>[] parametros = constructores[0].getParameterTypes();
		return constructores.length == 1 && parametros.length == 0;
	}

	private Object instanciarObjetoUsando(Constructor<?> constructor, Class<?> solicitante) {
		List<Object> argumentos = new ArrayList<Object>();
		for (Class<?> tipoDeParametro : constructor.getParameterTypes()) {
			argumentos.add(this.obtenerObjeto(tipoDeParametro, solicitante));
		}
		
		try {
			Object[] array = argumentos.toArray();
			return constructor.newInstance(array);
		} catch (InstantiationException e) {
			throw new RuntimeException();
		} catch (IllegalAccessException e) {
			throw new RuntimeException();
		} catch (IllegalArgumentException e) {
			throw new RuntimeException();
		} catch (InvocationTargetException e) {
			throw new RuntimeException();
		}
	}

	private Boolean puedoUsarEsteConstructor(Constructor<?> unConstructor, Class<?> solicitante) {
		//allSatisfy
		for (Class<?> tipoDeParametro : unConstructor.getParameterTypes())
			if (!contexto.puedoInstanciarUn(tipoDeParametro, solicitante))
				return false;
		
		return true;
	}
}
