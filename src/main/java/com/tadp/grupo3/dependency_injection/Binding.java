package com.tadp.grupo3.dependency_injection;

public class Binding {

	private Class<?> tipoBase;
	private Class<?> tipoConcreto;
	
	public Binding(Class<?> tipoBase, Class<?> tipoConcreto) {
		this.setTipoBase(tipoBase);
		this.setTipoConcreto(tipoConcreto);
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

}
