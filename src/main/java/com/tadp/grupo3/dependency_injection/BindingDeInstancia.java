package com.tadp.grupo3.dependency_injection;

public class BindingDeInstancia implements Binding {
	
	private String id;
	private Class<?> tipo;
	private Object instancia;

	public BindingDeInstancia(String id, Object instancia) {
		this.setId(id);
		this.setTipo(instancia.getClass());
		this.setInstancia(instancia);
	}

	public Object getInstancia() {
		return instancia;
	}

	private void setInstancia(Object objetoPrimitivo) {
		this.instancia = objetoPrimitivo;
	}
	

	//TODO: Me parece que este metodo no tiene sentido ahora.
	//   	Igualmente lo dejo porque hay que implementarlo por la herencia.
	public boolean esValidoPara(Class<?> otroScope, Class<?> tipoInstancia) {
		return true;
		//		return this.getScope().equals(otroScope) && tipoInstancia.isAssignableFrom(this.getTipo());
	}

	public Class<?> getTipo() {
		return tipo;
	}

	private void setTipo(Class<?> tipo) {
		this.tipo = tipo;
	}

	public Object obtenerObjeto(Class<?> _, Class<?> __) {
		return this.getInstancia();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
