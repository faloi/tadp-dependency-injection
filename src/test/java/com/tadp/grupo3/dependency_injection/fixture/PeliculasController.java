package com.tadp.grupo3.dependency_injection.fixture;

import java.util.List;

public class PeliculasController {

	private PeliculasHome homePeliculas;
	private UsuariosHome homeUsuarios;
	private List<PeliculasHome> homesPeliculas;
	
	public String cadena;
	
	public PeliculasController(PeliculasHome unHome){
	      this.setHomePeliculas(unHome);
	}
	
	public PeliculasController(List<PeliculasHome> unosHomes){
	      this.setHomesPeliculas(unosHomes);
	}

	public PeliculasController(UsuariosHome unHomeDeUsuarios) {
		this.homeUsuarios = unHomeDeUsuarios;
	}
	
	public PeliculasController(PeliculasHome unHomeDePeliculas, UsuariosHome unHomeDeUsuarios) {
		this(unHomeDePeliculas);
		this.setHomeUsuarios(unHomeDeUsuarios);
	}

	public UsuariosHome getHomeUsuarios() {
		return homeUsuarios;
	}

	public void setHomeUsuarios(UsuariosHome homeUsuarios) {
		this.homeUsuarios = homeUsuarios;
	}
	
	public PeliculasHome getHomePeliculas() {
		return homePeliculas;
	}

	public void setHomePeliculas(PeliculasHome homePeliculas) {
		this.homePeliculas = homePeliculas;
	}

	public List<PeliculasHome> getHomesPeliculas() {
		return homesPeliculas;
	}
	
	public void setHomesPeliculas(List<PeliculasHome> homesPeliculas) {
		this.homesPeliculas = homesPeliculas;
	}
}
