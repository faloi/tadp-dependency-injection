package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnConstructorValidoException;
import com.tadp.grupo3.dependency_injection.exceptions.NoHayConstructorValidoException;

public class InyeccionPorConstructor extends EstrategiaInyeccion {

	public <T> T obtenerObjetoDesdeBindingDeClase(Class<T> claseAInstanciar, Class<?> solicitante) {
		Class<?> clasePosta = getContexto().obtenerTipoPostaPara(claseAInstanciar);
		
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
			argumentos.add(this.getContexto().obtenerObjeto(tipoDeParametro, solicitante));
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
			if (!this.puedoInstanciarUn(tipoDeParametro, solicitante))
				return false;
		
		return true;
	}
	
	public Boolean puedoInstanciarUn(Class<?> unTipo, Class<?> solicitante) {
		for(BindingEspecifico unBinding : this.getContexto().getBindingsEspecificos())
			if (unBinding.esValidoPara(solicitante, unTipo))
				return true;
		
		//anySatisfy
		for(Binding unBinding : this.getContexto().getBindings()){
			if(unBinding.esValidoPara(solicitante, unTipo))
				return true;
		}

		for(BindingDeInstancia unBinding : this.getContexto().getBindingsDeInstancia()){
			if(unBinding.esValidoPara(solicitante, unTipo))
				return true;
		}		
		
		return false;
	}
}
