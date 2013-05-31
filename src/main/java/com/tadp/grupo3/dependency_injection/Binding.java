package com.tadp.grupo3.dependency_injection;

public interface Binding {
	
	boolean esValidoPara(Class<?> solicitante, Class<?> tipoInstancia);
	Object obtenerObjeto(Class<?> solicitante, Class<?> tipoInstancia);
	
}