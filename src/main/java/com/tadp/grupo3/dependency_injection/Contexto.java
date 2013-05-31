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
	public Collection<Binding> getBindings() {
		return bindings;
	}
	private Collection<BindingDeInstancia> bindingsDeInstancia;
	public Collection<BindingDeInstancia> getBindingsDeInstancia() {
		return bindingsDeInstancia;
	}
	
	private Collection<BindingEspecifico> bindingsEspecificos;
	public Collection<BindingEspecifico> getBindingsEspecificos() {
		return bindingsEspecificos;
	}
	
	private EstrategiaInyeccion estrategia;
	
	public Contexto(TipoDeInyeccion tipo) {
		this.bindings = new ArrayList<Binding>();
		this.bindingsDeInstancia = new ArrayList<BindingDeInstancia>();
		this.bindingsEspecificos = new ArrayList<BindingEspecifico>();
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
	
	private void agregarBinding(Binding binding) {
		this.getBindings().add(binding);
	}
	
	public <TipoBase> void agregarBinding(Class<TipoBase> tipoBase, Class<?> tipoConcreto) {
		this.agregarBinding(new Binding(tipoBase, tipoConcreto));
	}
	
	public void agregarBindingDeInstancia(Class<?> scope, Object instancia) {
		this.getBindingsDeInstancia().add(new BindingDeInstancia(scope, instancia));
	}
	
	public void configurarBindingEspecifico(Class<?> unTipo, Object... parametrosDelConstructor) {
		this.getBindingsEspecificos().add(new BindingEspecifico(unTipo, parametrosDelConstructor));
	}

	Class<?> obtenerTipoPostaPara(Class<?> tipoBase) {
		List<Binding> bindings = filter(having(on(Binding.class).esTipoBase(tipoBase)), this.getBindings());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return bindings.get(0).getTipoConcreto();
	}

	public <T> T obtenerObjeto(Class<T> claseAInstanciar) {
		return estrategia.obtenerObjeto(claseAInstanciar);
	}
}
