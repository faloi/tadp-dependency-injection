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
		
		assertArrayEquals(home.dameTodasTusPeliculas(), new String[] {"The Hangover", "One flew over the cuckoo’s nest"});
	}

	@Test(expected=RuntimeException.class)
	public void test2() {
		this.contexto.obtenerInstancia(PersonaHome.class);
	}

}
