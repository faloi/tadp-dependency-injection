package com.tadp.grupo3.dependency_injection;

public interface Binding {
	
	boolean esValidoPara(Class<?> solicitante, Class<?> tipoInstancia);
	<TipoInstancia> TipoInstancia obtenerObjeto(Class<?> solicitante, Class<TipoInstancia> tipoInstancia);
	
}