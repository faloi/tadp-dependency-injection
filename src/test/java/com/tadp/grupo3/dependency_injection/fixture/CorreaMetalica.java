package com.tadp.grupo3.dependency_injection.fixture;

import com.tadp.grupo3.dependency_injection.Inyectar;

public class CorreaMetalica implements CorreaDePerro {

	private Perro perro;
	
	public CorreaMetalica(Perro perro) {
		this.setPerro(perro);
	}
	
	public Perro getPerro() {
		return this.perro;
	}

	@Inyectar
	public void setPerro(Perro perro) {
		this.perro = perro;
	}

}
