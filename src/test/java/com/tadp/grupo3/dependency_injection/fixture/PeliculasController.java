package com.tadp.grupo3.dependency_injection.fixture;

public class PeliculasController {

	private PeliculasHome home;
	public String cadena;
	
	public PeliculasController(PeliculasHome unHome){
	      this.setHome(unHome);
	}

	public PeliculasHome getHome() {
		return home;
	}

	public void setHome(PeliculasHome home) {
		this.home = home;
	}
}
