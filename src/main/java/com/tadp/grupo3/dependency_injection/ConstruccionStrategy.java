package com.tadp.grupo3.dependency_injection;

public interface ConstruccionStrategy {
	public <T> T instanciar(Class<T> unaClase, Contexto unContexto);
}
