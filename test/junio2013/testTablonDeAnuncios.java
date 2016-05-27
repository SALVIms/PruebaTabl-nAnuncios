package junio2013;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;

public class testTablonDeAnuncios {

	TablonDeAnuncios tablon = null;

	@Before
	public void inicializa() {
		tablon = new TablonDeAnuncios();
	}

	@After
	public void finaliza() {
		tablon = null;
	}

	// Test 1: Comprobar que inicialmente hay un anuncio en el tablón
	@Test
	public void inicialmenteUnAnuncioEnTablonDevuelve1() {
		assertEquals(1, tablon.anunciosPublicados());
		// Al crear un Tablon se crea automaticamente un anuncio de bienvenida.
	}

	// Test 2: Crear un anuncio del anunciante "LA EMPRESA", insertarlo, y
	// comprobar que se ha aumentado en uno el número de anuncios del tablón.
	@Test
	public void publicarAnuncioEmpresaYComprobarNumeroDeAnunciosDevuelveNumeroDeAnuncios() {
		int anunciosTotales = tablon.anunciosPublicados() + 1;
		// Inicial + 1.

		Anuncio anuncio = null;
		anuncio = new Anuncio("AnuncioTitulo", "AnuncioTexto", "LA EMPRESA");

		IBaseDeDatosDeAnunciantes bdAnunciantes = null;
		IBaseDeDatosDePagos bdPagos = null;

		tablon.publicarAnuncio(anuncio, bdAnunciantes, bdPagos);

		assertEquals(anunciosTotales, tablon.anunciosPublicados());

	}

	// Test 3: Publicar un anuncio de un anunciante existente que no sea
	// "LA EMPRESA" y comprobar que si no tiene fondos no se inserta.

	@Test
	public void publicarAnuncioDeAnuncianteExistenteComprobarSiTieneFondos() {
		int anunciosTotales = tablon.anunciosPublicados(); // Inicial

		Anuncio anuncio = null;
		anuncio = new Anuncio("AnuncioTitulo", "AnuncioTexto", "ANUNCIANTE");

		IBaseDeDatosDePagos bdPagos = (IBaseDeDatosDePagos) mock(IBaseDeDatosDePagos.class);
		IBaseDeDatosDeAnunciantes bdAnunciantes = (IBaseDeDatosDeAnunciantes) mock(IBaseDeDatosDeAnunciantes.class);

		when(bdAnunciantes.buscarAnunciante(anuncio.anunciante_)).thenReturn(
				true);

		when(bdPagos.anuncianteTieneSaldo(anuncio.anunciante_))
				.thenReturn(true);

		when(!bdPagos.anuncianteTieneSaldo(anuncio.anunciante_)).thenReturn(
				false);

		tablon.publicarAnuncio(anuncio, bdAnunciantes, bdPagos);
		assertEquals(anunciosTotales, tablon.anunciosPublicados());

		verify(bdAnunciantes).buscarAnunciante(anuncio.anunciante_);
		verify(bdPagos).anuncianteTieneSaldo(anuncio.anunciante_);
		// verify(bdPagos).anuncioPublicado(anuncio.anunciante_);
	}

	// Test 4: Publicar un anuncio de un anunciante existente y solvente que no
	// sea "LA EMPRESA", verificando que se ha invocado el método
	// IBaseDeDatosDePagos.anuncioPublicado()

	@Test
	public void publicarAnuncioAnuncianteExistenteSolventeYVerificarMetodoBaseDeDatosDePagos() {
		int anunciosTotales = tablon.anunciosPublicados(); // Inicial

		Anuncio anuncio = null;
		anuncio = new Anuncio("AnuncioTitulo", "AnuncioTexto", "ANUNCIANTE");

		IBaseDeDatosDePagos bdPagos = (IBaseDeDatosDePagos) mock(IBaseDeDatosDePagos.class);
		IBaseDeDatosDeAnunciantes bdAnunciantes = (IBaseDeDatosDeAnunciantes) mock(IBaseDeDatosDeAnunciantes.class);

		when(bdAnunciantes.buscarAnunciante(anuncio.anunciante_)).thenReturn(
				true);

		when(bdPagos.anuncianteTieneSaldo(anuncio.anunciante_))
				.thenReturn(true);

		when(!bdPagos.anuncianteTieneSaldo(anuncio.anunciante_)).thenReturn(
				false);

		tablon.publicarAnuncio(anuncio, bdAnunciantes, bdPagos);
		assertEquals(anunciosTotales, tablon.anunciosPublicados());
		verify(bdPagos).anuncianteTieneSaldo(anuncio.anunciante_);

	}

	// Test 5: Publicar dos anuncios del anunciante "LA EMPRESA", buscar el
	// segundo por su título y comprobar que está y que el tamaño del tablón no
	// aumenta
	@Test
	public void publicarDosAnunciosDeEmpresaBuscarSegundoPorTituloYComprobarNombreYTamaño() {
		int cantidadEsperada = tablon.anunciosPublicados() + 1;

		Anuncio anuncio1, anuncio2 = null;
		anuncio1 = new Anuncio("TextoAnuncioUNO", "TextoUNO", "LA EMPRESA");
		// Este se publica
		anuncio2 = new Anuncio("TextoAnuncioDOS", "TextoDos", "LA EMPRESA");
		// Este tambien se publica
		IBaseDeDatosDeAnunciantes bdAnunciantes = null;
		IBaseDeDatosDePagos bdPagos = null;
		tablon.publicarAnuncio(anuncio1, bdAnunciantes, bdPagos);
		if (tablon.buscarAnuncioPorTitulo("Prueba de Anuncio") == null) {
			// Busco si existe el segundo anuncio antes de publicar
			tablon.publicarAnuncio(anuncio2, bdAnunciantes, bdPagos);
		}

		assertEquals(cantidadEsperada, tablon.anunciosPublicados());

	}

	// Test 6: Publicar dos anuncios del anunciante "LA EMPRESA" , borrar el
	// primero y comprobar que si se busca ya no está en el tablón.
	@Test
	public void publicarDosAnunciosDeEmpresaYBorrarElPrimeroComprobarQueNoEsta() {
		Anuncio resultadoEsperado = null;

		Anuncio anuncio, anuncio2 = null;
		anuncio = new Anuncio("Prueba de Anuncio", "Anuncio cosas",
				"LA EMPRESA");
		anuncio2 = new Anuncio("Prueba de Anuncio 2", "Anuncio cosas",
				"LA EMPRESA");
		IBaseDeDatosDeAnunciantes bdAnunciantes = null;
		IBaseDeDatosDePagos bdPagos = null;
		tablon.publicarAnuncio(anuncio, bdAnunciantes, bdPagos);
		tablon.publicarAnuncio(anuncio2, bdAnunciantes, bdPagos);
		tablon.borrarAnuncio("Prueba de Anuncio", "LA EMPRESA");
		Anuncio resultado = tablon.buscarAnuncioPorTitulo("Prueba de Anuncio");

		assertEquals(resultadoEsperado, resultado);

	}

	// Test 7: Comprobar que si se intenta publicar un anuncio ya existente
	// (mismo título y mismo enunciante) no se inserta. Hay que modificar la
	// clase TablonDeAnuncios para pasar este test.

	@Test
	public void publicarAnuncioYaExistenteNoSeInserta() {
		int cantidadEsperada = tablon.anunciosPublicados() + 1;

		Anuncio anuncio1, anuncio2 = null;
		anuncio1 = new Anuncio("TextoAnuncio", "Texto", "LA EMPRESA");
		// Este se publica
		anuncio2 = new Anuncio("TextoAnuncio", "Texto", "LA EMPRESA");
		// Este NO se publica
		IBaseDeDatosDeAnunciantes bdAnunciantes = null;
		IBaseDeDatosDePagos bdPagos = null;
		tablon.publicarAnuncio(anuncio1, bdAnunciantes, bdPagos);
		if (tablon.buscarAnuncioPorTitulo("Prueba de Anuncio") == null) {
			// Busco si existe el segundo anuncio antes de publicar
			tablon.publicarAnuncio(anuncio2, bdAnunciantes, bdPagos);
		}

		assertEquals(cantidadEsperada, tablon.anunciosPublicados());
	}

	// Test 8: . Comprobar que si se intenta publicar un anuncio y el anunciante
	// no existe se eleva la excepción AnuncianteNoExisteException. Hay que
	// modificar la clase TablonDeAnuncios para pasar este test.
	@Test
	public void publicarAnuncioYAnuncianteNoExisteDevuelveExcepcion() {
		
	}

}
