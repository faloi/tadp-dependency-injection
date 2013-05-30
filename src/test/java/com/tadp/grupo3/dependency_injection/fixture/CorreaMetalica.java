package com.tadp.grupo3.dependency_injection.fixture;

public class CorreaMetalica implements CorreaDePerro {

	private Perro perro;
	
	public CorreaMetalica(Perro perro) {
		this.setPerro(perro);
	}
	
	public Perro getPerro() {
		return this.perro;
	}

	public void setPerro(Perro perro) {
		this.perro = perro;
	}

}
