package com.tadp.grupo3.dependency_injection.fixture;

public class PeliculasController {

	public PeliculasHome home;
	public String cadena;
	
	public PeliculasController(PeliculasHome unHome){
	      this.home = unHome;
	}
}
