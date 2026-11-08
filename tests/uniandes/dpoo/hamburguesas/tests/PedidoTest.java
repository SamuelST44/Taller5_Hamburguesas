package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    private Pedido pedido;
    private ProductoMenu corral;
    private ProductoMenu papas;
    private File archivo;

    @BeforeEach
    void setUp( ) throws Exception
    {
        pedido = new Pedido( "Thomas", "Calle 19 # 1-50" );
        corral = new ProductoMenu( "corral", 14000 );
        papas = new ProductoMenu( "papas medianas", 5500 );
        archivo = File.createTempFile( "factura_prueba", ".txt" );
    }

    @AfterEach
    void tearDown( )
    {
        archivo.delete( );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Thomas", pedido.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testIdsConsecutivos( )
    {
        Pedido otro = new Pedido( "Ana", "Carrera 7" );
        assertEquals( pedido.getIdPedido( ) + 1, otro.getIdPedido( ), "Los pedidos deben tener identificadores consecutivos." );
    }

    @Test
    void testPrecioTotalPedidoVacio( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ), "Un pedido vacío debe costar 0." );
    }

    @Test
    void testPrecioTotalPedido( )
    {
        pedido.agregarProducto( corral );
        pedido.agregarProducto( papas );
        // neto 19500, IVA 3705, total 23205
        assertEquals( 23205, pedido.getPrecioTotalPedido( ), "El precio total debe ser neto + IVA." );
    }

    @Test
    void testPrecioTotalConProductoAjustado( )
    {
        ProductoAjustado ajustado = new ProductoAjustado( corral );
        ajustado.agregarIngrediente( new Ingrediente( "huevo", 2500 ) );
        pedido.agregarProducto( ajustado );
        // neto 16500, IVA 3135, total 19635
        assertEquals( 19635, pedido.getPrecioTotalPedido( ), "El precio debe incluir los ingredientes agregados." );
    }

    @Test
    void testGenerarTextoFacturaVacio( )
    {
        String esperado = "Cliente: Thomas\n" + "Dirección: Calle 19 # 1-50\n" + "----------------\n" + "----------------\n" + "Precio Neto:  0\n" + "IVA:          0\n"
                + "Precio Total: 0\n";
        assertEquals( esperado, pedido.generarTextoFactura( ), "La factura de un pedido vacío no es la esperada." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        pedido.agregarProducto( corral );
        pedido.agregarProducto( papas );
        String esperado = "Cliente: Thomas\n" + "Dirección: Calle 19 # 1-50\n" + "----------------\n" + "corral\n" + "            14000\n" + "papas medianas\n"
                + "            5500\n" + "----------------\n" + "Precio Neto:  19500\n" + "IVA:          3705\n" + "Precio Total: 23205\n";
        assertEquals( esperado, pedido.generarTextoFactura( ), "La factura no es la esperada." );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        pedido.agregarProducto( corral );
        pedido.guardarFactura( archivo );
        String contenido = new String( Files.readAllBytes( archivo.toPath( ) ) );
        assertEquals( pedido.generarTextoFactura( ), contenido, "El archivo debe tener el texto de la factura." );
    }

    @Test
    void testGuardarFacturaRutaInvalida( )
    {
        File invalido = new File( "./carpeta_que_no_existe/factura.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( invalido ), "Debe lanzar FileNotFoundException si la ruta no existe." );
    }
}