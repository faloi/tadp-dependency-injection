package com.tadp.grupo3.dependency_injection.fixture;

public abstract class PeliculasHome {
	
	private Logger logger;
	public Logger getLogger() {
		return this.logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public abstract String[] dameTodasTusPeliculas();
	
}
