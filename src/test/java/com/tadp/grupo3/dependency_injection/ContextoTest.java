package com.tadp.grupo3.dependency_injection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tadp.grupo3.dependency_injection.exceptions.*;
import com.tadp.grupo3.dependency_injection.fixture.*;
public class ContextoTest {

	private Contexto contexto;

	@Before
	public void setUp() {
		this.contexto = new Contexto(TipoDeInyeccion.PorConstructor);
	}
	
	@Test
	public void obtenerObjeto_crea_un_objeto_del_tipo_especificado_en_el_binding() {
		this.contexto.agregarBinding(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		PeliculasHome home = contexto.obtenerObjeto(PeliculasHome.class);
		
		assertThat(home, instanceOf(EnMemoriaPeliculasHome.class));
	}

	@Test
	public void obtenerObjeto_crea_objetos_de_los_tipos_especificados_en_los_bindings() {
		this.contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		this.contexto.agregarBinding(Perro.class, Bulldog.class);

		Perro perro = contexto.obtenerObjeto(Perro.class);
		PeliculasHome home = contexto.obtenerObjeto(PeliculasHome.class);
		
		assertThat(home, instanceOf(SqlPeliculasHome.class));
		assertThat(perro, instanceOf(Bulldog.class));
	}
	
	@Test(expected=NoExisteBindingException.class)
	public void obtenerObjeto_explota_si_no_hay_bindings_para_el_tipo_solicitado() {
		this.contexto.obtenerObjeto(PersonaHome.class);
	}
	
	@Test(expected=MasDeUnBindingException.class)
	public void obtenerObjeto_explota_si_hay_mas_de_un_binding_para_el_tipo_solicitado() {
		this.contexto.agregarBinding(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		this.contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		
		this.contexto.obtenerObjeto(PeliculasHome.class);
	}
	
	@Test(expected = NoHayConstructorValidoException.class)
	public void obtenerObjeto_explota_si_no_hay_ningun_constructor_valido(){
		contexto.agregarBinding(PeliculasController.class, PeliculasController.class);
		contexto.obtenerObjeto(PeliculasController.class);
	}

	@Test(expected = MasDeUnConstructorValidoException.class)
	public void obtenerObjeto_explota_si_se_podria_instanciar_usando_mas_de_un_constructor(){
		contexto.agregarBinding(PeliculasHome.class, SqlPeliculasHome.class);
		contexto.agregarBinding(UsuariosHome.class, MongoDbUsuariosHome.class);
		contexto.agregarBinding(PeliculasController.class, PeliculasController.class);
		
		contexto.obtenerObjeto(PeliculasController.class);
	}
	
	@Test
	public void obtenerObjeto_crea_recursivamente_las_dependencias_del_tipo_dado() {
		contexto.agregarBinding(Logger.class, MongoDbLogger.class);
		contexto.agregarBinding(PeliculasHome.class, MongoDbPeliculasHome.class);
		contexto.agregarBinding(PeliculasController.class, PeliculasController.class);
		
		PeliculasController unController = contexto.obtenerObjeto(PeliculasController.class);
		
		assertThat(unController.getHomePeliculas(), instanceOf(MongoDbPeliculasHome.class));
		assertThat(unController.getHomePeliculas().getLogger(), instanceOf(MongoDbLogger.class));
	}
	
	@Test
	public void obtenerObjeto_usa_los_bindings_de_instancia_para_resolver_dependencias() {
		MongoDbLogger mongoDbLogger = new MongoDbLogger();
		contexto.agregarBindingDeInstancia(MongoDbPeliculasHome.class, mongoDbLogger);
		contexto.agregarBinding(MongoDbPeliculasHome.class, MongoDbPeliculasHome.class);
		
		MongoDbPeliculasHome home = contexto.obtenerObjeto(MongoDbPeliculasHome.class);
		
		assertEquals(mongoDbLogger, home.getLogger());
	}
	
	@Test
	public void obtenerObjeto_puede_crear_colecciones_correctamente() {
		List<PeliculasHome> homesPeliculas = new ArrayList<PeliculasHome>();
		homesPeliculas.add(new SqlPeliculasHome());
		homesPeliculas.add(new MongoDbPeliculasHome(new MongoDbLogger()));
		
		contexto.agregarBinding(PeliculasController.class, PeliculasController.class);
		contexto.agregarBindingDeInstancia(PeliculasController.class, homesPeliculas);
		
		PeliculasController unController = contexto.obtenerObjeto(PeliculasController.class);
		
		assertTrue(unController.getHomesPeliculas().get(0) instanceof SqlPeliculasHome);
		assertTrue(unController.getHomesPeliculas().get(1) instanceof MongoDbPeliculasHome);
	}
	
	@Test
	public void obtenerObjeto_por_accessors_instancia_a_varios_niveles() {
		contexto.establecerEstrategia(TipoDeInyeccion.PorAccessors);
		
		contexto.agregarBinding(Logger.class, MongoDbLogger.class);
		contexto.agregarBinding(PeliculasHome.class, MdxPeliculasHome.class);
		contexto.agregarBinding(CineController.class, CineController.class);
		 
		CineController unController = contexto.obtenerObjeto(CineController.class);
		
		assertTrue(unController.getPeliculasHome() instanceof MdxPeliculasHome);
		assertTrue(unController.getPeliculasHome().getLogger() instanceof MongoDbLogger);
	}
}
