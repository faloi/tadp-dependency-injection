package com.tadp.grupo3.dependency_injection;

public class BindingDeInstancia implements Binding {
	
	private Class<?> scope;
	private Class<?> tipo;
	private Object instancia;

	public BindingDeInstancia(Class<?> scope, Object instancia) {
		this.setScope(scope);
		this.setTipo(instancia.getClass());
		this.setInstancia(instancia);
	}

	public Object getInstancia() {
		return instancia;
	}

	private void setInstancia(Object objetoPrimitivo) {
		this.instancia = objetoPrimitivo;
	}

	private Class<?> getScope() {
		return scope;
	}

	private void setScope(Class<?> scope) {
		this.scope = scope;
	}

	public boolean esValidoPara(Class<?> otroScope, Class<?> tipoInstancia) {
		return this.getScope().equals(otroScope) && tipoInstancia.isAssignableFrom(this.getTipo());
	}

	public Class<?> getTipo() {
		return tipo;
	}

	private void setTipo(Class<?> tipo) {
		this.tipo = tipo;
	}

	public <TipoInstancia> TipoInstancia obtenerObjeto(Class<?> _, Class<TipoInstancia> __) {
		return (TipoInstancia) this.getInstancia();
	}

}
