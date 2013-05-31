package com.tadp.grupo3.dependency_injection.fixture;

public class CineController {
	public PeliculasController peliculasController;
	public PeliculasHome homePeliculas;
	
	public CineController() { }
	
	public PeliculasController getPeliculasController() {
		return peliculasController;
	}

	public void setPeliculasController(PeliculasController peliculasController) {
		this.peliculasController = peliculasController;
	}

	public PeliculasHome getHomePeliculas() {
		return homePeliculas;
	}

	public void setHomePeliculas(PeliculasHome homePeliculas) {
		this.homePeliculas = homePeliculas;
	}
}
