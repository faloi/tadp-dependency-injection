package com.tadp.grupo3.dependency_injection;

public class PeliculasController {

	public PeliculasHome home;
	public String cadena;
	
	public PeliculasController(PeliculasHome unHome){
	      this.home = unHome;
	}
}
