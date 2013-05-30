package com.tadp.grupo3.dependency_injection.fixture;

public class MongoDbPeliculasHome extends PeliculasHome {

	public MongoDbPeliculasHome(Logger logger) {
		this.setLogger(logger);
	}
	
	@Override
	public String[] dameTodasTusPeliculas() {
		// TODO Auto-generated method stub
		return null;
	}

}
