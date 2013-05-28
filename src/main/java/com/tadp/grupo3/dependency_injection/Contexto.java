package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;

import net.sf.staccatocommons.collections.stream.Streams;
import static net.sf.staccatocommons.lambda.Lambda.$;
import static net.sf.staccatocommons.lambda.Lambda.lambda;
import net.sf.staccatocommons.collections.stream.Stream;

public class Contexto {

	// Properties
	private Collection<Binding<Class<?>>> bindings;
	private Collection<Binding<Class<?>>> getBindings() {
		return bindings;
	}
	
	private Collection<BindingPrimitivo> bindingsPrimitivos;
	private Collection<BindingPrimitivo> getBindingsPrimitivos() {
		return bindingsPrimitivos;
	}
	
	public Contexto() {
		this.bindings = new ArrayList<Binding<Class<?>>>();
		this.bindingsPrimitivos = new ArrayList<BindingPrimitivo>();
	}
	
	public <TipoBase> void agregarBinding(Class<TipoBase> tipoBase, Class<?> tipoConcreto) {
		this.agregarBinding(new Binding<TipoBase>(tipoBase, tipoConcreto));
	}

	public <T> T obtenerInstancia(Class<T> tipoBase) {
		try {
			for (Binding<?> binding : this.getBindings()) {
				if (binding.getTipoBase().equals(tipoBase))
					return (T) binding.getTipoConcreto().newInstance();
			}
			
			throw new NoExisteBindingException();
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
		for (BindingPrimitivo binding : this.getBindingsPrimitivos()) {
			if (binding.getScope().equals(scope))
				return (TipoPrimitivo) binding.getObjetoPrimitivo();
		}
		
		throw new NoExisteBindingException();		
	}

}
