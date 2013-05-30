package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.lambdaj.function.convert.Converter;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnConstructorValidoException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoHayConstructorValidoException;
import com.tadp.grupo3.dependency_injection.fixture.PeliculasController;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

public class Contexto {

	// Properties
	private Collection<Binding> bindings;
	private Collection<Binding> getBindings() {
		return bindings;
	}
	
	private Collection<BindingPrimitivo> bindingsPrimitivos;
	private Collection<BindingPrimitivo> getBindingsPrimitivos() {
		return bindingsPrimitivos;
	}
	
	public Contexto() {
		this.bindings = new ArrayList<Binding>();
		this.bindingsPrimitivos = new ArrayList<BindingPrimitivo>();
	}
	
	public <TipoBase> void agregarBinding(Class<TipoBase> tipoBase, Class<?> tipoConcreto) {
		this.agregarBinding(new Binding(tipoBase, tipoConcreto));
	}

	public <T> T obtenerInstancia(Class<T> tipoBase) {
		try {
			List<Binding> bindings = filter(having(on(Binding.class).esTipoBase(tipoBase)), this.getBindings());
			
			if (bindings.isEmpty())
				throw new NoExisteBindingException();
			
			if (bindings.size() > 1)
				throw new MasDeUnBindingException();
			
			return (T) bindings.get(0).getTipoConcreto().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
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

	public void agregarBindingPrimitivo(Class<?> scope, Object objetoPrimitivo) {
		this.getBindingsPrimitivos().add(new BindingPrimitivo(scope, objetoPrimitivo));
	}
	
	private void agregarBinding(Binding binding) {
		this.getBindings().add(binding);
	}

	public <TipoPrimitivo> TipoPrimitivo obtenerObjetoPrimitivoPara(Class<?> scope, Class<TipoPrimitivo> tipoPrimitivo) {
		List<BindingPrimitivo> bindings = filter(having(on(BindingPrimitivo.class).esScope(scope)), this.getBindingsPrimitivos());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return (TipoPrimitivo) bindings.get(0).getObjetoPrimitivo();
	}

	public <T> T creameUnObjeto(Class<T> claseAInstanciar) {
		Constructor<?>[] constructores = claseAInstanciar.getConstructors();
		
		List<Constructor<?>> constructoresValidos = new ArrayList<Constructor<?>>();
		for (Constructor<?> constructor : constructores) {
			if (this.puedoUsarEsteConstructor(constructor))
				constructoresValidos.add(constructor);
		}
		
		if (constructoresValidos.isEmpty())
			throw new NoHayConstructorValidoException();
		
		if (constructoresValidos.size() > 1)
			throw new MasDeUnConstructorValidoException();
		
		return (T) this.instanciarObjetoUsando(constructoresValidos.get(0));	
	}

	private Object instanciarObjetoUsando(Constructor<?> constructor) {
		List<Object> argumentos = new ArrayList<Object>();
		for (Class<?> tipoDeParametro : constructor.getParameterTypes()) {
			argumentos.add(this.obtenerInstancia(tipoDeParametro));
		}
		
		try {
			return constructor.newInstance(argumentos.toArray());
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

	private Boolean puedoUsarEsteConstructor(Constructor<?> unConstructor) {
		//allSatisfy
		for (Class<?> tipoDeParametro : unConstructor.getParameterTypes())
			if (!this.puedoInstanciarUn(tipoDeParametro))
				return false;
		
		return true;
	}

	private Boolean puedoInstanciarUn(Class<?> unTipo) {
		//anySatisfy
		for(Binding unBinding : bindings){
			if(unBinding.getTipoBase().equals(unTipo))
				return true;
		}

		return false;
	}

}
