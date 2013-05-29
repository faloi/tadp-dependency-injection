package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;
import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

public class Contexto {

	// Properties
	private Collection<Binding> bindings;
	public Collection<Binding> getBindings() {
		return bindings;
	}
	
	private Collection<BindingInstancia> bindingsInstancias;
	private Collection<BindingInstancia> getBindingsDeStrings() {
		return bindingsInstancias;
	}
	
	public Contexto() {
		this.bindings = new ArrayList<Binding>();
		this.bindingsInstancias = new ArrayList<BindingInstancia>();
	}
	
	public <TipoBase> void agregarBinding(Class<TipoBase> tipoBase, Class<?> tipoConcreto) {
		this.agregarBinding(new Binding(tipoBase, tipoConcreto));
	}

	public <T> T obtenerInstancia(Class<T> tipoBase){
		return this.obtenerInstancia(tipoBase,new PorConstructorStrategy());
	}
	
	public <T> T obtenerInstancia(Class<T> tipoBase, ConstruccionStrategy unStrategy) {
		return unStrategy.instanciar(tipoBase,this);
		/*		try {
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
		}*/
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

	public void agregarBindingInstancia(Class<?> scope, Object objetoPrimitivo) {
		if(objetoPrimitivo instanceof String){
			BindingInstancia unBindingDeInstancia = new BindingInstancia(scope, objetoPrimitivo);
			unBindingDeInstancia.setTipo(String.class);
			this.getBindingsDeStrings().add(unBindingDeInstancia);
		}else{
			
		}
	}
	
	private void agregarBinding(Binding binding) {
		this.getBindings().add(binding);
	}

	public <TipoPrimitivo> TipoPrimitivo obtenerObjetoPrimitivoPara(Class<?> scope, Class<TipoPrimitivo> tipoPrimitivo) {
		List<BindingInstancia> bindings = filter(having(on(BindingInstancia.class).esScope(scope)), this.getBindingsDeStrings());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return (TipoPrimitivo) bindings.get(0).getObjetoPrimitivo();
	}

	
	public void comprobarIntanciacionDelParametro(Class<?> unParametro){
		//Aplanamos la coleccion a mano.
		for(BindingInstancia unBindingDeInstancia : bindingsInstancias){
			if(unBindingDeInstancia.getScope().equals(unParametro))
				return;
		}
		for(Binding unBinding : bindings){
			if(unBinding.getTipoBase().equals(unParametro))
				return;
		}
		throw new NoExisteBindingException();		
	}
	
	public void comprobarInstanciacionDelConstructor(Constructor unConstructor){
		Class<?>[] unosParametros = unConstructor.getParameterTypes();
		
		for(Class<?> unParametro : unosParametros)
			this.comprobarIntanciacionDelParametro(unParametro);		
	}

}
