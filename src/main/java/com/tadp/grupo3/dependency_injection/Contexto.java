package com.tadp.grupo3.dependency_injection;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
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
	private EstrategiaInyeccion getEstrategia() {
		return estrategia;
	}
	public void setEstrategia(EstrategiaInyeccion estrategiaDeInyeccion) {
		estrategiaDeInyeccion.setContexto(this);
		this.estrategia = estrategiaDeInyeccion;
	}

	public Contexto(EstrategiaInyeccion estrategiaDeInyeccion) {
		this.bindings = new ArrayList<Binding>();
		this.bindingsDeInstancia = new ArrayList<BindingDeInstancia>();
		this.bindingsEspecificos = new ArrayList<BindingEspecifico>();
		
		this.setEstrategia(estrategiaDeInyeccion);
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
		return this.obtenerObjeto(claseAInstanciar, claseAInstanciar);
	}
	
	public <T> T obtenerObjeto(Class<T> claseAInstanciar, Class<?> solicitante) {
		try {
			return this.obtenerObjetoDesdeBindingEspecifico(solicitante, claseAInstanciar);
		} catch (Exception e) {
			try {
				return this.obtenerObjetoDesdeBindingDeInstancia(solicitante, claseAInstanciar);
			} catch (Exception e2) {
				return this.getEstrategia().obtenerObjetoDesdeBindingDeClase(claseAInstanciar, solicitante);	
			}
		}
	}
	
	private <TipoInstancia> TipoInstancia obtenerObjetoDesdeBindingEspecifico(Class<?> solicitante, Class<TipoInstancia> _) {
		List<BindingEspecifico> bindings = filter(having(on(BindingEspecifico.class).esValidoPara(solicitante, _)), this.getBindingsEspecificos());
		
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
		List<BindingDeInstancia> bindings = filter(having(on(BindingDeInstancia.class).esValidoPara(scope, tipoInstancia)), this.getBindingsDeInstancia());
		
		if (bindings.isEmpty())
			throw new NoExisteBindingException();
		
		if (bindings.size() > 1)
			throw new MasDeUnBindingException();
		
		return (TipoInstancia) bindings.get(0).getInstancia();
	}

}
