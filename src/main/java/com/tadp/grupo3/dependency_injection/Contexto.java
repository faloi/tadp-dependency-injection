package com.tadp.grupo3.dependency_injection;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnConstructorValidoException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoHayConstructorValidoException;

public class Contexto {

	// Properties
	private Collection<Binding> bindings;
	private Collection<Binding> getBindings() {
		return bindings;
	}
	
	private Collection<BindingDeInstancia> bindingsDeInstancia;
	private Collection<BindingDeInstancia> getBindingsDeInstancia() {
		return bindingsDeInstancia;
	}
	
	public Contexto() {
		this.bindings = new ArrayList<Binding>();
		this.bindingsDeInstancia = new ArrayList<BindingDeInstancia>();
	}
	
	public <TipoBase> void agregarBinding(Class<TipoBase> tipoBase, Class<?> tipoConcreto) {
		this.agregarBinding(new Binding(tipoBase, tipoConcreto));
	}

	private Class<?> obtenerTipoPostaPara(Class<?> tipoBase) {
		List<Binding> bindings = filter(having(on(Binding.class).esTipoBase(tipoBase)), this.getBindings());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return bindings.get(0).getTipoConcreto();
	}
	
	public void agregarBindingDeInstancia(Class<?> scope, Object instancia) {
		this.getBindingsDeInstancia().add(new BindingDeInstancia(scope, instancia));
	}
	
	private void agregarBinding(Binding binding) {
		this.getBindings().add(binding);
	}

	public <T> T obtenerObjeto(Class<T> claseAInstanciar) {
		return obtenerObjeto(claseAInstanciar, claseAInstanciar);
	}

	private <T> T obtenerObjeto(Class<T> claseAInstanciar, Class<?> solicitante) {
		try {
			return this.obtenerObjetoDesdeBindingDeInstancia(solicitante, claseAInstanciar);
		} catch (Exception e) {
			return this.obtenerObjetoDesdeBindingDeClase(claseAInstanciar, solicitante);	
		}
	}
	
	private <TipoInstancia> TipoInstancia obtenerObjetoDesdeBindingDeInstancia(Class<?> scope, Class<TipoInstancia> tipoInstancia) {
		List<BindingDeInstancia> bindings = filter(having(on(BindingDeInstancia.class).esValidoPara(scope, tipoInstancia)), this.getBindingsDeInstancia());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return (TipoInstancia) bindings.get(0).getInstancia();
	}

	private <T> T obtenerObjetoDesdeBindingDeClase(Class<T> claseAInstanciar, Class<?> solicitante) {
		Class<?> clasePosta = this.obtenerTipoPostaPara(claseAInstanciar);
		
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
			if (!this.puedoInstanciarUn(tipoDeParametro, solicitante))
				return false;
		
		return true;
	}

	private Boolean puedoInstanciarUn(Class<?> unTipo, Class<?> solicitante) {
		//anySatisfy
		for(Binding unBinding : this.getBindings()){
			if(unBinding.esValidoPara(solicitante, unTipo))
				return true;
		}

		for(BindingDeInstancia unBinding : this.getBindingsDeInstancia()){
			if(unBinding.esValidoPara(solicitante, unTipo))
				return true;
		}		
		
		return false;
	}

}
