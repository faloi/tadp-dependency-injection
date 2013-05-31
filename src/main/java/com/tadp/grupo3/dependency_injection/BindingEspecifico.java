package com.tadp.grupo3.dependency_injection;

public class BindingEspecifico {

	private Class<?> tipoBase;
	private Object[] parametrosDelConstructor;

	public BindingEspecifico(Class<?> tipoBase, Object[] parametrosDelConstructor) {
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
}
