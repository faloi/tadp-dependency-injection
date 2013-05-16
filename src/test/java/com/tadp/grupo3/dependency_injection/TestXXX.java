package com.tadp.grupo3.dependency_injection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestXXX {

	private Contexto contexto;

	@Before
	public void setUp() {
		this.contexto = Contexto.getInstance();
	}
	
	@Test
	public void test1() {
		this.contexto.agregarBinding(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		PeliculasHome home = (PeliculasHome) contexto.obtenerInstancia(PeliculasHome.class);
		
		assertArrayEquals(new String[] {"The Hangover", "One flew over the cuckoo’s nest"}, home.dameTodasTusPeliculas());
	}

	@Test(expected=RuntimeException.class)
	public void test2() {
		this.contexto.obtenerInstancia(PersonaHome.class);
	}
	
	public void test3() {
		contexto.agregarBindingPrimitivo(SqlPeliculasHome.class, "...cadena de conexion a SQL...");
		SqlPeliculasHome home = (SqlPeliculasHome) contexto.obtenerInstancia(PeliculasHome.class);
		
		assertEquals("...cadena de conexion a SQL...", home.getCadenaDeConexion());
	}

}
