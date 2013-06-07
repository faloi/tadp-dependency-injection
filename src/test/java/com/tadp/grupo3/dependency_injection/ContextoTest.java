package com.tadp.grupo3.dependency_injection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.MasDeUnConstructorValidoException;
import com.tadp.grupo3.dependency_injection.exceptions.NoExisteBindingException;
import com.tadp.grupo3.dependency_injection.exceptions.NoHayConstructorValidoException;
import com.tadp.grupo3.dependency_injection.exceptions.NoTieneConstructorVacioException;
import com.tadp.grupo3.dependency_injection.exceptions.YaEstaUsadoElIdException;
import com.tadp.grupo3.dependency_injection.fixture.Bulldog;
import com.tadp.grupo3.dependency_injection.fixture.CineController;
import com.tadp.grupo3.dependency_injection.fixture.CorreaDePerro;
import com.tadp.grupo3.dependency_injection.fixture.CorreaMetalica;
import com.tadp.grupo3.dependency_injection.fixture.EnMemoriaPeliculasHome;
import com.tadp.grupo3.dependency_injection.fixture.GmailLogger;
import com.tadp.grupo3.dependency_injection.fixture.Logger;
import com.tadp.grupo3.dependency_injection.fixture.MailSender;
import com.tadp.grupo3.dependency_injection.fixture.MdxPeliculasHome;
import com.tadp.grupo3.dependency_injection.fixture.MongoDbLogger;
import com.tadp.grupo3.dependency_injection.fixture.MongoDbPeliculasHome;
import com.tadp.grupo3.dependency_injection.fixture.MongoDbUsuariosHome;
import com.tadp.grupo3.dependency_injection.fixture.PeliculasController;
import com.tadp.grupo3.dependency_injection.fixture.PeliculasHome;
import com.tadp.grupo3.dependency_injection.fixture.Perro;
import com.tadp.grupo3.dependency_injection.fixture.PersonaHome;
import com.tadp.grupo3.dependency_injection.fixture.SqlPeliculasHome;
import com.tadp.grupo3.dependency_injection.fixture.Usuario;
import com.tadp.grupo3.dependency_injection.fixture.UsuariosHome;
public class ContextoTest {

	private Contexto contexto;

	@Before
	public void setUp() {
		this.contexto = new Contexto(new InyeccionPorConstructor());
	}
	
	@Test
	public void obtenerObjeto_crea_un_objeto_del_tipo_especificado_en_el_binding() {
		this.contexto.agregarBindingDeClase(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		PeliculasHome home = contexto.obtenerObjeto(PeliculasHome.class);
		
		assertThat(home, instanceOf(EnMemoriaPeliculasHome.class));
	}

	@Test
	public void obtenerObjeto_crea_objetos_de_los_tipos_especificados_en_los_bindings() {
		this.contexto.agregarBindingDeClase(PeliculasHome.class, SqlPeliculasHome.class);
		this.contexto.agregarBindingDeClase(Perro.class, Bulldog.class);

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
		this.contexto.agregarBindingDeClase(PeliculasHome.class, EnMemoriaPeliculasHome.class);
		this.contexto.agregarBindingDeClase(PeliculasHome.class, SqlPeliculasHome.class);
		
		this.contexto.obtenerObjeto(PeliculasHome.class);
	}
	
	@Test(expected = NoHayConstructorValidoException.class)
	public void obtenerObjeto_explota_si_no_hay_ningun_constructor_valido(){
		contexto.agregarBindingDeClase(PeliculasController.class, PeliculasController.class);
		contexto.obtenerObjeto(PeliculasController.class);
	}

	@Test(expected = MasDeUnConstructorValidoException.class)
	public void obtenerObjeto_explota_si_se_podria_instanciar_usando_mas_de_un_constructor(){
		contexto.agregarBindingDeClase(PeliculasHome.class, SqlPeliculasHome.class);
		contexto.agregarBindingDeClase(UsuariosHome.class, MongoDbUsuariosHome.class);
		contexto.agregarBindingDeClase(PeliculasController.class, PeliculasController.class);
		
		contexto.obtenerObjeto(PeliculasController.class);
	}
	
	@Test
	public void obtenerObjeto_crea_recursivamente_las_dependencias_del_tipo_dado() {
		contexto.agregarBindingDeClase(Logger.class, MongoDbLogger.class);
		contexto.agregarBindingDeClase(PeliculasHome.class, MongoDbPeliculasHome.class);
		contexto.agregarBindingDeClase(PeliculasController.class, PeliculasController.class);
		
		PeliculasController unController = contexto.obtenerObjeto(PeliculasController.class);
		
		assertThat(unController.getHomePeliculas(), instanceOf(MongoDbPeliculasHome.class));
		assertThat(unController.getHomePeliculas().getLogger(), instanceOf(MongoDbLogger.class));
	}
	
	@Test
	public void obtenerObjeto_usa_binding_de_instancia_resolviendo_dependecia_por_id(){
		MongoDbLogger mongoDbLogger = new MongoDbLogger();
		contexto.agregarBindingDeInstancia("mongoDbLoggerTest", mongoDbLogger);
		assertTrue(contexto.obtenerObjeto("mongoDbLoggerTest") instanceof MongoDbLogger);
	}
	
//	@Test
//	public void obtenerObjeto_usa_los_bindings_de_instancia_para_resolver_dependencias() {
//		MongoDbLogger mongoDbLogger = new MongoDbLogger();
//		contexto.agregarBindingDeInstancia("mongoDbLoggerTest", mongoDbLogger);
//		contexto.agregarBindingDeClase(MongoDbPeliculasHome.class, MongoDbPeliculasHome.class);
//		
//		MongoDbPeliculasHome home = contexto.obtenerObjeto(MongoDbPeliculasHome.class);
//		
//		assertEquals(mongoDbLogger, home.getLogger());
//	}
	
//	@Test
//	public void obtenerObjeto_puede_crear_colecciones_correctamente() {
//		List<PeliculasHome> homesPeliculas = new ArrayList<PeliculasHome>();
//		homesPeliculas.add(new SqlPeliculasHome());
//		homesPeliculas.add(new MongoDbPeliculasHome(new MongoDbLogger()));
//		
//		contexto.agregarBindingDeClase(PeliculasController.class, PeliculasController.class);
//		contexto.agregarBindingDeInstancia(PeliculasController.class, homesPeliculas);
//		
//		PeliculasController unController = contexto.obtenerObjeto(PeliculasController.class);
//		
//		assertTrue(unController.getHomesPeliculas().get(0) instanceof SqlPeliculasHome);
//		assertTrue(unController.getHomesPeliculas().get(1) instanceof MongoDbPeliculasHome);
//	}
	
	@Test
	public void obtenerObjeto_por_accessors_instancia_a_varios_niveles() {
		contexto.setEstrategia(new InyeccionPorAccessors());
		
		contexto.agregarBindingDeClase(Logger.class, MongoDbLogger.class);
		contexto.agregarBindingDeClase(PeliculasHome.class, MdxPeliculasHome.class);
		contexto.agregarBindingDeClase(CineController.class, CineController.class);
		 
		CineController unController = contexto.obtenerObjeto(CineController.class);
		
		assertTrue(unController.getPeliculasHome() instanceof MdxPeliculasHome);
		assertTrue(unController.getPeliculasHome().getLogger() instanceof MongoDbLogger);
	}

//	@Test
//	public void obtenerObjeto_por_accessors_solo_usa_setters_publicos() {
//		contexto.setEstrategia(new InyeccionPorAccessors());
//		
//		MailSender unMailSender = new MailSender("federico.aloi", "mipasswordloco", "smtp.gmail.com", 3389);
//		contexto.agregarBindingDeInstancia(GmailLogger.class, unMailSender);
//		
//		contexto.agregarBindingDeInstancia(GmailLogger.class, new Usuario());
//		contexto.agregarBindingDeClase(GmailLogger.class, GmailLogger.class);
//		 
//		GmailLogger logger = contexto.obtenerObjeto(GmailLogger.class);
//
//		assertEquals(unMailSender, logger.getMailSender());
//		assertNull(logger.getUsuario());
//	}
	
	@Test(expected=NoTieneConstructorVacioException.class)
	public void obtenerObjeto_por_accessors_explota_si_el_objeto_no_tiene_constructor_vacio() {
		contexto.setEstrategia(new InyeccionPorAccessors());
		
		contexto.agregarBindingDeClase(Perro.class, Bulldog.class);
		contexto.agregarBindingDeClase(CorreaDePerro.class, CorreaMetalica.class);
		
		contexto.obtenerObjeto(CorreaDePerro.class);
	}
	
	@Test
	public void obtenerObjeto_usa_los_bindings_manuales() {
		contexto.agregarBindingManual(MailSender.class,"rodrigoExample", "rodri042@gmail.com", "notedoymiclave", "smtp.gmail.com", 3389);
		contexto.agregarBindingDeClase(MailSender.class, MailSender.class);
		
		MailSender unMailSender = (MailSender) contexto.obtenerObjeto("rodrigoExample");
		
		assertEquals("rodri042@gmail.com", unMailSender.getUsuario());
		assertEquals("notedoymiclave", unMailSender.getPassword());
		assertEquals("smtp.gmail.com", unMailSender.getSmtp());
		assertEquals((Integer)3389, unMailSender.getPuerto());
	}
	
	@Test(expected=YaEstaUsadoElIdException.class)
	public void agrega_dos_binding_de_instancia_con_mismo_id_y_explota() {
		
		MongoDbLogger mongoDbLogger = new MongoDbLogger();
		
		contexto.agregarBindingDeInstancia("mongoDbLoggerTest", mongoDbLogger);
		contexto.agregarBindingDeInstancia("mongoDbLoggerTest", mongoDbLogger);
	}	
	
	@Test(expected=NoHayConstructorValidoException.class)
	public void obtenerObjeto_via_binding_manual_explota_si_no_puede_usar_ningun_constructor() {
		contexto.agregarBindingManual(PeliculasController.class, "falopa");
		
		contexto.obtenerObjeto(PeliculasController.class);
	}
}
