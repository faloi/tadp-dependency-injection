package com.tadp.grupo3.dependency_injection;

public class BindingInstancia {
	
	private Class<?> scope;
	private Object objetoPrimitivo;
	private Class<?> tipo;

	public BindingInstancia(Class<?> scope, Object objetoPrimitivo) {
		this.setScope(scope);
		this.setObjetoPrimitivo(objetoPrimitivo);
	}

	public void setTipo(Class<?> unTipo){
		this.tipo = unTipo;
	}
	
	public Class<?> getTipo(){
		return tipo;
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
