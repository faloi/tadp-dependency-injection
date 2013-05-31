package com.tadp.grupo3.dependency_injection.fixture;

import com.tadp.grupo3.dependency_injection.Inyectar;

public class CineController {
	public PeliculasHome peliculasHome;
	
	public CineController() { }
	
	public PeliculasHome getPeliculasHome() {
		return peliculasHome;
	}

	@Inyectar
	public void setPeliculasHome(PeliculasHome peliculasHome) {
		this.peliculasHome = peliculasHome;
	}
}
