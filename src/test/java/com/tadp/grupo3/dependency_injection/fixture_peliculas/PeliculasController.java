package com.tadp.grupo3.dependency_injection.fixture_peliculas;

public class PeliculasController {

	public PeliculasHome home;
	public String cadena;
	
	public PeliculasController(PeliculasHome unHome){
	      this.home = unHome;
	}
}
