package com.tadp.grupo3.dependency_injection;

public abstract class EstrategiaInyeccion {
	private Contexto contexto; 
	protected Contexto getContexto() {
		return contexto;
	}
	public void setContexto(Contexto contexto) {
		this.contexto = contexto;
	}

	abstract <T> T obtenerObjetoDesdeBindingDeClase(Class<T> claseAInstanciar, Class<?> solicitante);
}
