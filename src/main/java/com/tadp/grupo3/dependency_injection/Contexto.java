package com.tadp.grupo3.dependency_injection;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.YaEstaUsadoElIdException;

public class Contexto {
	// Properties
	private Collection<BindingDeClase> bindings;
	private Collection<BindingDeClase> getBindingsDeClase() {
		return bindings;
	}

	private Collection<BindingDeInstancia> bindingsEspecificos;
	private Collection<BindingDeInstancia> getBindingsEspecificos() {
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
		this.bindingsEspecificos = new ArrayList<BindingDeInstancia>();
		
		this.setEstrategia(estrategiaDeInyeccion);
	}

	public void agregarBindingDeClase(Class<?> tipoBase, Class<?> tipoConcreto) {
		this.getBindingsDeClase().add(new BindingDeClase(tipoBase, tipoConcreto));
	}
	
	public void agregarBindingDeInstancia(String id, Object instancia) {
		for(BindingDeInstancia binding: this.bindingsEspecificos){
			if(binding.getId().equals(id))
				throw new YaEstaUsadoElIdException();
		}
		this.bindingsEspecificos.add(new BindingDeInstancia(id, instancia));
	}
	
	public void agregarBindingManual(Class<?> unTipo,String id, Object... parametrosDelConstructor) {
		
		this.agregarBindingDeInstancia(id, new BindingManual(unTipo, parametrosDelConstructor).obtenerObjeto(unTipo, null));
	}

	private Class<?> obtenerTipoPostaPara(Class<?> tipoBase) {
		List<BindingDeClase> bindings = filter(having(on(BindingDeClase.class).esTipoBase(tipoBase)), this.getBindingsDeClase());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return bindings.get(0).getTipoConcreto();
	}

	public Object obtenerObjeto(String id){
		for(BindingDeInstancia binding : this.bindingsEspecificos){
			if(binding.getId().equals(id))
				return binding.obtenerObjeto(null, null);
		}
		throw new NoExisteBindingException();
	}
	
	public <T> T obtenerObjeto(Class<T> claseAInstanciar) {
		return this.obtenerObjeto(claseAInstanciar, claseAInstanciar);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T obtenerObjeto(Class<T> claseAInstanciar, Class<?> solicitante) {
		try {
			return (T) this.obtenerObjetoDesdeBindingEspecifico(solicitante, claseAInstanciar);
		} catch (NoExisteBindingException e) {
			Class<?> tipoPosta = this.obtenerTipoPostaPara(claseAInstanciar);
			return (T) this.getEstrategia().obtenerObjeto(tipoPosta, solicitante);	
		}
	}
	
	private Object obtenerObjetoDesdeBindingEspecifico(Class<?> solicitante, Class<?> tipoInstancia) {
		List<BindingDeInstancia> bindingsUtiles = filter(having(on(Binding.class).esValidoPara(solicitante, tipoInstancia)), this.getBindingsEspecificos());
		
		if (bindingsUtiles.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindingsUtiles.size() > 1)
			throw new MasDeUnBindingException();
		
		return bindingsUtiles.get(0).obtenerObjeto(solicitante, tipoInstancia);		
	}

	public Boolean sePuedeInstanciar(Class<?> unTipo, Class<?> solicitante) {
		for(Binding unBinding : this.getBindingsEspecificos())
			if (unBinding.esValidoPara(solicitante, unTipo))
				return true;
		
		//anySatisfy
		for(BindingDeClase unBinding : this.getBindingsDeClase()){
			if(unBinding.esValidoPara(solicitante, unTipo))
				return true;
		}
		
		return false;
	}
}
