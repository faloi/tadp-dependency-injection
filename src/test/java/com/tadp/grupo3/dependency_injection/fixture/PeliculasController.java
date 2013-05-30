package com.tadp.grupo3.dependency_injection.fixture;

public class PeliculasController {

	private PeliculasHome homePeliculas;
	private UsuariosHome homeUsuarios;
	
	public String cadena;
	
	public PeliculasController(PeliculasHome unHome){
	      this.setPeliculasHome(unHome);
	}

	public PeliculasController(UsuariosHome unHomeDeUsuarios) {
		this.homeUsuarios = unHomeDeUsuarios;
	}
	
	public PeliculasController(PeliculasHome unHomeDePeliculas, UsuariosHome unHomeDeUsuarios) {
		this(unHomeDePeliculas);
		this.setHomeUsuarios(unHomeDeUsuarios);
	}
	
	public PeliculasHome getPeliculasHome() {
		return homePeliculas;
	}

	public void setPeliculasHome(PeliculasHome home) {
		this.homePeliculas = home;
	}

	public UsuariosHome getHomeUsuarios() {
		return homeUsuarios;
	}

	public void setHomeUsuarios(UsuariosHome homeUsuarios) {
		this.homeUsuarios = homeUsuarios;
	}
}
