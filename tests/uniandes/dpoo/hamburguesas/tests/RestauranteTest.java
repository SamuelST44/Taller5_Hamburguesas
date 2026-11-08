package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private Restaurante restaurante;
    private File ingredientes;
    private File menu;
    private File combos;

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );
        ingredientes = crearArchivo( "lechuga;1000\n\ntomate;1000\nqueso mozzarella;2500\n" );
        menu = crearArchivo( "corral;14000\npapas medianas;5500\n\ngaseosa;5000\n" );
        combos = crearArchivo( "combo corral;10%;corral;papas medianas;gaseosa\n\n" );
    }

    @AfterEach
    void tearDown( )
    {
        ingredientes.delete( );
        menu.delete( );
        combos.delete( );
    }

    private File crearArchivo( String contenido ) throws Exception
    {
        File archivo = File.createTempFile( "restaurante_prueba", ".txt" );
        PrintWriter out = new PrintWriter( archivo );
        out.print( contenido );
        out.close( );
        return archivo;
    }

    @Test
    void testRestauranteVacio( )
    {
        assertEquals( 0, restaurante.getIngredientes( ).size( ), "No debe haber ingredientes." );
        assertEquals( 0, restaurante.getMenuBase( ).size( ), "No debe haber productos." );
        assertEquals( 0, restaurante.getMenuCombos( ).size( ), "No debe haber combos." );
        assertEquals( 0, restaurante.getPedidos( ).size( ), "No debe haber pedidos." );
        assertNull( restaurante.getPedidoEnCurso( ), "No debe haber pedido en curso." );
    }

    @Test
    void testCargarInformacionPrueba( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
        assertEquals( 3, restaurante.getIngredientes( ).size( ), "Deben cargarse 3 ingredientes." );
        assertEquals( 3, restaurante.getMenuBase( ).size( ), "Deben cargarse 3 productos." );
        assertEquals( 1, restaurante.getMenuCombos( ).size( ), "Debe cargarse 1 combo." );
        assertEquals( "tomate", restaurante.getIngredientes( ).get( 1 ).getNombre( ), "El ingrediente no es el esperado." );
        assertEquals( 5500, restaurante.getMenuBase( ).get( 1 ).getPrecio( ), "El precio del producto no es el esperado." );
        assertEquals( 22050, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ), "El precio del combo no es el esperado." );
    }

    @Test
    void testCargarArchivosDelTaller( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( new File( "./data/ingredientes.txt" ), new File( "./data/menu.txt" ), new File( "./data/combos.txt" ) );
        assertEquals( 15, restaurante.getIngredientes( ).size( ), "Deben cargarse 15 ingredientes." );
        assertEquals( 22, restaurante.getMenuBase( ).size( ), "Deben cargarse 22 productos." );
        assertEquals( 4, restaurante.getMenuCombos( ).size( ), "Deben cargarse 4 combos." );
    }

    @Test
    void testIngredienteRepetido( ) throws Exception
    {
        File repetidos = crearArchivo( "lechuga;1000\nlechuga;1000\n" );
        assertThrows( IngredienteRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( repetidos, menu, combos ),
                "Debe lanzar IngredienteRepetidoException." );
        repetidos.delete( );
    }

    @Test
    void testProductoRepetido( ) throws Exception
    {
        File repetidos = crearArchivo( "corral;14000\ncorral;14000\n" );
        assertThrows( ProductoRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( ingredientes, repetidos, combos ),
                "Debe lanzar ProductoRepetidoException." );
        repetidos.delete( );
    }

    @Test
    void testComboRepetido( ) throws Exception
    {
        File repetidos = crearArchivo( "combo corral;10%;corral;gaseosa\ncombo corral;10%;corral;gaseosa\n" );
        assertThrows( ProductoRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( ingredientes, menu, repetidos ),
                "Debe lanzar ProductoRepetidoException para combos repetidos." );
        repetidos.delete( );
    }

    @Test
    void testComboConProductoFaltante( ) throws Exception
    {
        File faltante = crearArchivo( "combo raro;10%;corral;malteada\n" );
        assertThrows( ProductoFaltanteException.class, ( ) -> restaurante.cargarInformacionRestaurante( ingredientes, menu, faltante ),
                "Debe lanzar ProductoFaltanteException." );
        faltante.delete( );
    }

    @Test
    void testArchivoNoExiste( )
    {
        File noExiste = new File( "./data/no_existe.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> restaurante.cargarInformacionRestaurante( noExiste, menu, combos ),
                "Debe lanzar FileNotFoundException." );
    }

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Thomas", "Calle 19" );
        assertNotNull( restaurante.getPedidoEnCurso( ), "Debe haber un pedido en curso." );
        assertEquals( "Thomas", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El cliente no es el esperado." );
    }

    @Test
    void testIniciarPedidoConPedidoEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Thomas", "Calle 19" );
        assertThrows( YaHayUnPedidoEnCursoException.class, ( ) -> restaurante.iniciarPedido( "Ana", "Carrera 7" ),
                "Debe lanzar YaHayUnPedidoEnCursoException." );
    }

    @Test
    void testCerrarSinPedido( )
    {
        assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ), "Debe lanzar NoHayPedidoEnCursoException." );
    }

    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
        restaurante.iniciarPedido( "Thomas", "Calle 19" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        pedido.agregarProducto( restaurante.getMenuBase( ).get( 0 ) );

        restaurante.cerrarYGuardarPedido( );

        assertNull( restaurante.getPedidoEnCurso( ), "Después de cerrar no debe haber pedido en curso." );
        assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debe quedar en la lista de pedidos." );
        assertSame( pedido, restaurante.getPedidos( ).get( 0 ), "El pedido guardado no es el esperado." );
        File factura = new File( "./facturas/factura_" + pedido.getIdPedido( ) + ".txt" );
        assertTrue( factura.exists( ), "Debe existir el archivo de la factura." );
        factura.delete( );
    }
}