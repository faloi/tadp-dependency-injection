package com.tadp.grupo3.dependency_injection.fixture;

import com.tadp.grupo3.dependency_injection.Inyectar;

public class CineController {
	private PeliculasController peliculasController;
	private PeliculasHome homePeliculas;
	
	public CineController() { }
	
	public PeliculasController getPeliculasController() {
		return peliculasController;
	}

	public PeliculasHome getHomePeliculas() {
		return homePeliculas;
	}
	
	@Inyectar
	public void setPeliculasController(PeliculasController peliculasController) {
		this.peliculasController = peliculasController;
	}
	
	@Inyectar
	public void setHomePeliculas(PeliculasHome homePeliculas) {
		this.homePeliculas = homePeliculas;
	}
}
