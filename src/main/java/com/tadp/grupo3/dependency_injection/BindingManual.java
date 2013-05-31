package com.tadp.grupo3.dependency_injection;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tadp.grupo3.dependency_injection.exceptions.NoHayConstructorValidoException;

public class BindingManual implements Binding {

	private Class<?> tipoBase;
	private Object[] parametrosDelConstructor;

	public BindingManual(Class<?> tipoBase, Object[] parametrosDelConstructor) {
		this.setTipoBase(tipoBase);
		this.setParametrosDelConstructor(parametrosDelConstructor);
	}

	public boolean esTipoBase(Class<?> otroTipo) {
		return this.getTipoBase().equals(otroTipo);
	}
	
	public Class<?> getTipoBase() {
		return tipoBase;
	}

	public void setTipoBase(Class<?> tipoBase) {
		this.tipoBase = tipoBase;
	}
	
	public void setParametrosDelConstructor(Object[] parametrosDelConstructor) {
		this.parametrosDelConstructor = parametrosDelConstructor;
	}
	
	public Object[] getParametrosDelConstructor() {
		return parametrosDelConstructor;
	}
	
	public boolean esValidoPara(Class<?> solicitante, Class<?> _) {
		return this.getTipoBase().equals(solicitante);
	}
	
	public Object obtenerObjeto(Class<?> solicitante, Class<?> _) {
		List<Class<?>> tipoDeParametros = new ArrayList<Class<?>>();
		for (Object parametro : this.getParametrosDelConstructor()) {
			tipoDeParametros.add(parametro.getClass());
		}
		
		Constructor<?>[] constructores = solicitante.getConstructors();
		for (Constructor<?> constructor : constructores) {
			if (Arrays.equals(constructor.getParameterTypes(), tipoDeParametros.toArray()))
				try {
					return constructor.newInstance(parametrosDelConstructor);
				} catch (Exception e) {
					throw new RuntimeException();
				}
		}
		
		throw new NoHayConstructorValidoException();
	}
}
