package com.tadp.grupo3.dependency_injection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;
import com.tadp.grupo3.dependency_injection.fixture.CorreaDePerro;
import com.tadp.grupo3.dependency_injection.fixture.CorreaMetalica;
import com.tadp.grupo3.dependency_injection.fixture.Perro;

public class TestXXX {

	private Contexto contexto;

	@Before
	public void setUp() {
		this.contexto = new Contexto();
	}
	
	@Test
	public void test1() {
		this.contexto.agregarBinding(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		PeliculasHome home = contexto.obtenerInstancia(PeliculasHome.class);
		
		assertThat(home, instanceOf(EnMemoriaPeliculasHome.class));
	}

	@Test
	public void test1b() {
		this.contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		PeliculasHome home = contexto.obtenerInstancia(PeliculasHome.class);
		
		assertThat(home, instanceOf(SqlPeliculasHome.class));
	}
	
	@Test
	public void test1c() {
		this.contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		this.contexto.agregarBinding(Perro.class, Bulldog.class);

		Perro perro = contexto.obtenerInstancia(Perro.class);
		PeliculasHome home = contexto.obtenerInstancia(PeliculasHome.class);
		
		assertThat(home, instanceOf(SqlPeliculasHome.class));
		assertThat(perro, instanceOf(Bulldog.class));
	}
	
	@Test(expected=NoExisteBindingException.class)
	public void test2() {
		this.contexto.obtenerInstancia(PersonaHome.class);
	}
//	
//	@Test
//	public void test3() {
//		contexto.agregarBindingPrimitivo(SqlPeliculasHome.class, "...cadena de conexion a SQL...");
//		SqlPeliculasHome sqlHome = (SqlPeliculasHome) contexto.obtenerInstancia(PeliculasHome.class);
//		
//		assertEquals("...cadena de conexion a SQL...", sqlHome.getCadenaDeConexion());
//	}
//	
//	@Test(expected=MasDeUnBindingException.class)
//	public void test4() {
//		this.contexto.agregarBinding(PeliculasHome.class, EnMemoriaPeliculasHome.class);
//		this.contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
//		
//		this.contexto.obtenerInstancia(PeliculasHome.class);
//	}
//	
//	@Test
//	public void test5() {
//		this.contexto.agregarBinding(Perro.class, Bulldog.class);
//		this.contexto.agregarBinding(CorreaDePerro.class, CorreaMetalica.class);
//		
//		CorreaDePerro correa = this.contexto.obtenerInstancia(CorreaDePerro.class);
//		
//		assertNotNull(correa);
//		assertThat(correa, instanceOf(CorreaMetalica.class));
//
//		assertNotNull(correa.getPerro());
//		assertThat(correa.getPerro(), instanceOf(Bulldog.class));
//	}

}
