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
	private Collection<BindingDeClase> bindings;
	public Collection<BindingDeClase> getBindingsDeClase() {
		return bindings;
	}

	private Collection<Binding> bindingsEspecificos;
	public Collection<Binding> getBindingsEspecificos() {
		return bindingsEspecificos;
	}
	
	private EstrategiaInyeccion estrategia;
	private EstrategiaInyeccion getEstrategia() {
		return estrategia;
	}
	
	public void setEstrategia(EstrategiaInyeccion estrategiaDeInyeccion) {
		estrategiaDeInyeccion.setContexto(this);
		this.estrategia = estrategiaDeInyeccion;
	}

	public Contexto(EstrategiaInyeccion estrategiaDeInyeccion) {
		this.bindings = new ArrayList<BindingDeClase>();
		this.bindingsEspecificos = new ArrayList<Binding>();
		
		this.setEstrategia(estrategiaDeInyeccion);
	}

	
	public void agregarBindingDeClase(Class<?> tipoBase, Class<?> tipoConcreto) {
		this.getBindingsDeClase().add(new BindingDeClase(tipoBase, tipoConcreto));
	}
	
	public void agregarBindingDeInstancia(Class<?> scope, Object instancia) {
		this.getBindingsEspecificos().add(new BindingDeInstancia(scope, instancia));
	}
	
	public void agregarBindingManual(Class<?> unTipo, Object... parametrosDelConstructor) {
		this.getBindingsEspecificos().add(new BindingManual(unTipo, parametrosDelConstructor));
	}

	Class<?> obtenerTipoPostaPara(Class<?> tipoBase) {
		List<BindingDeClase> bindings = filter(having(on(BindingDeClase.class).esTipoBase(tipoBase)), this.getBindingsDeClase());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return bindings.get(0).getTipoConcreto();
	}

	public <T> T obtenerObjeto(Class<T> claseAInstanciar) {
		return this.obtenerObjeto(claseAInstanciar, claseAInstanciar);
	}
	
	public <T> T obtenerObjeto(Class<T> claseAInstanciar, Class<?> solicitante) {
		try {
			return this.obtenerObjetoDesdeBindingEspecifico(solicitante, claseAInstanciar);
		} catch (Exception e2) {
			return this.getEstrategia().obtenerObjetoDesdeBindingDeClase(claseAInstanciar, solicitante);	
		}
	}
	
	private <T> T obtenerObjetoDesdeBindingEspecifico(Class<?> solicitante, Class<T> tipoInstancia) {
		List<Binding> bindingsUtiles = filter(having(on(Binding.class).esValidoPara(solicitante, tipoInstancia)), this.getBindingsEspecificos());
		
		if (bindingsUtiles.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindingsUtiles.size() > 1)
			throw new MasDeUnBindingException();
		
		return bindingsUtiles.get(0).obtenerObjeto(solicitante, tipoInstancia);		
	}

}
