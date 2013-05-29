package com.tadp.grupo3.dependency_injection;

public class BindingInstancia {
	
	private Class<?> scope;
	private Object objetoPrimitivo;

	public BindingInstancia(Class<?> scope, Object objetoPrimitivo) {
		this.setScope(scope);
		this.setObjetoPrimitivo(objetoPrimitivo);
	}

	public Object getObjetoPrimitivo() {
		return objetoPrimitivo;
	}

	public void setObjetoPrimitivo(Object objetoPrimitivo) {
		this.objetoPrimitivo = objetoPrimitivo;
	}

	public Class<?> getScope() {
		return scope;
	}

	public void setScope(Class<?> scope) {
		this.scope = scope;
	}

	public Boolean esScope(Class<?> otroScope) {
		return this.getScope().equals(otroScope);
	}

}
