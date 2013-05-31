package com.tadp.grupo3.dependency_injection;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;

public class Contexto {
	// Properties
	private Collection<Binding> bindings;
	private Collection<Binding> getBindings() {
		return bindings;
	}
	private Collection<BindingDeInstancia> bindingsDeInstancia;
	public Collection<BindingDeInstancia> getBindingsDeInstancia() {
		return bindingsDeInstancia;
	}
	
	private EstrategiaInyeccion estrategia;
	private EstrategiaInyeccion getEstrategia() {
		return estrategia;
	}
	
	public Contexto(TipoDeInyeccion tipo) {
		this.bindings = new ArrayList<Binding>();
		this.bindingsDeInstancia = new ArrayList<BindingDeInstancia>();
		this.establecerEstrategia(tipo);
	}

	public void establecerEstrategia(TipoDeInyeccion tipo) {
		switch(tipo) {
			case PorConstructor:
				this.estrategia = new InyeccionPorConstructor(this);
				break;
			case PorAccessors:
				this.estrategia = new InyeccionPorAccessors(this);
				break;
		}
	}
	
	public <TipoBase> void agregarBinding(Class<TipoBase> tipoBase, Class<?> tipoConcreto) {
		this.agregarBinding(new Binding(tipoBase, tipoConcreto));
	}

	Class<?> obtenerTipoPostaPara(Class<?> tipoBase) {
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
		return estrategia.obtenerObjeto(claseAInstanciar);
	}

	public Boolean puedoInstanciarUn(Class<?> unTipo, Class<?> solicitante) {
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
