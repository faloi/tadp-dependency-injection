package com.tadp.grupo3.dependency_injection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.tadp.grupo3.dependency_injection.exceptions.*;
import com.tadp.grupo3.dependency_injection.fixture.*;
public class TestXXX {

	private Contexto contexto;

	@Before
	public void setUp() {
		this.contexto = new Contexto();
	}
	
	@Test
	public void obtenerInstancia_crea_un_objeto_del_tipo_especificado_en_el_binding() {
		this.contexto.agregarBinding(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		PeliculasHome home = contexto.obtenerInstancia(PeliculasHome.class);
		
		assertThat(home, instanceOf(EnMemoriaPeliculasHome.class));
	}

	@Test
	public void obtenerInstancia_crea_objetos_de_los_tipos_especificados_en_los_bindings() {
		this.contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		this.contexto.agregarBinding(Perro.class, Bulldog.class);

		Perro perro = contexto.obtenerInstancia(Perro.class);
		PeliculasHome home = contexto.obtenerInstancia(PeliculasHome.class);
		
		assertThat(home, instanceOf(SqlPeliculasHome.class));
		assertThat(perro, instanceOf(Bulldog.class));
	}
	
	@Test(expected=NoExisteBindingException.class)
	public void obtenerInstancia_explota_si_no_hay_bindings_para_el_tipo_solicitado() {
		this.contexto.obtenerInstancia(PersonaHome.class);
	}
	
	@Test(expected=MasDeUnBindingException.class)
	public void obtenerInstancia_explota_si_hay_mas_de_un_binding_para_el_tipo_solicitado() {
		this.contexto.agregarBinding(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		this.contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		
		this.contexto.obtenerInstancia(PeliculasHome.class);
	}
	
	@Test
	public void obtenerObjetoPrimitivoPara_retorna_el_objeto_configurado_para_el_scope_dado() {
		contexto.agregarBindingPrimitivo(SqlPeliculasHome.class, "...cadena de conexion a SQL...");
		contexto.agregarBindingPrimitivo(EnMemoriaPeliculasHome.class, "0x88AB82");
		
		String cadenaConexion = contexto.obtenerObjetoPrimitivoPara(SqlPeliculasHome.class, String.class);
		String offset = contexto.obtenerObjetoPrimitivoPara(EnMemoriaPeliculasHome.class, String.class);
		
		assertEquals("...cadena de conexion a SQL...", cadenaConexion);
		assertEquals("0x88AB82", offset);
	}

	@Test
	public void test1(){
		contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		PeliculasController unController = contexto.creameUnObjeto(PeliculasController.class);

		assertThat(unController.getPeliculasHome(), instanceOf(SqlPeliculasHome.class));
	}

	@Test(expected = NoHayConstructorValidoException.class)
	public void test2(){
		PeliculasController unController = contexto.creameUnObjeto(PeliculasController.class);
		assertThat(unController.getPeliculasHome(), instanceOf(SqlPeliculasHome.class));
	}

	@Test(expected = MasDeUnConstructorValidoException.class)
	public void test3(){
		contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		contexto.agregarBinding(UsuariosHome.class, MongoDbUsuariosHome.class);
		
		contexto.creameUnObjeto(PeliculasController.class);
	}
	
	@Test
	public void test4() {
		contexto.agregarBinding(Logger.class, MongoDbLogger.class);
		contexto.agregarBinding(PeliculasHome.class, MongoDbPeliculasHome.class);
		
		PeliculasController unController = contexto.creameUnObjeto(PeliculasController.class);
		
		assertThat(unController.getPeliculasHome(), instanceOf(MongoDbPeliculasHome.class));
		assertThat(unController.getPeliculasHome().getLogger(), instanceOf(MongoDbLogger.class));
	}
	
//	@Test
//	public void test5() {
//		this.contexto.agregarBinding(Perro.class, Bulldog.class);
//		this.contexto.agregarBinding(CorreaDePerro.class, CorreaMetalica.class);
//		
//		CorreaDePerro correa = this.contexto.creameUnObjeto(CorreaDePerro.class);
//		
//		assertThat(correa, instanceOf(CorreaMetalica.class));
//		assertThat(correa.getPerro(), instanceOf(Bulldog.class));
//	}

}
