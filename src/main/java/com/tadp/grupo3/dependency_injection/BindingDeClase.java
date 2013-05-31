package com.tadp.grupo3.dependency_injection;

public class BindingDeClase {

	private Class<?> tipoBase;
	private Class<?> tipoConcreto;
	
	public BindingDeClase(Class<?> tipoBase, Class<?> tipoConcreto) {
		this.setTipoBase(tipoBase);
		this.setTipoConcreto(tipoConcreto);
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

	public Class<?> getTipoConcreto() {
		return tipoConcreto;
	}

	public void setTipoConcreto(Class<?> tipoConcreto) {
		this.tipoConcreto = tipoConcreto;
	}

	public boolean esValidoPara(Class<?> _, Class<?> unTipo) {
		return this.getTipoBase().equals(unTipo);
	}

}
