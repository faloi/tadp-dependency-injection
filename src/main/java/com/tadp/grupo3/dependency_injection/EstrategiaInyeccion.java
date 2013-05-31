package com.tadp.grupo3.dependency_injection;

public interface EstrategiaInyeccion {
	<T> T obtenerObjeto(Class<T> claseAInstanciar);
}
