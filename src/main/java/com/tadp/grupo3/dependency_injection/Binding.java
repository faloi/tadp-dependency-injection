package com.tadp.grupo3.dependency_injection;

public class Binding<TipoBase> {

	private Class<TipoBase> tipoBase;
	private Class<?> tipoConcreto;
	
	public Binding(Class<TipoBase> tipoBase, Class<?> tipoConcreto) {
		this.setTipoBase(tipoBase);
		this.setTipoConcreto(tipoConcreto);
	}

	public Class<TipoBase> getTipoBase() {
		return tipoBase;
	}

	public void setTipoBase(Class<TipoBase> tipoBase) {
		this.tipoBase = tipoBase;
	}

	public Class<?> getTipoConcreto() {
		return tipoConcreto;
	}

	public void setTipoConcreto(Class<?> tipoConcreto) {
		this.tipoConcreto = tipoConcreto;
	}

}
