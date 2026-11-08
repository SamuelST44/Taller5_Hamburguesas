package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoMenu base;
    private ProductoAjustado ajustado;
    private Ingrediente queso;
    private Ingrediente tocineta;
    private Ingrediente cebolla;

    @BeforeEach
    void setUp( )
    {
        base = new ProductoMenu( "corral", 14000 );
        ajustado = new ProductoAjustado( base );
        queso = new Ingrediente( "queso mozzarella", 2500 );
        tocineta = new Ingrediente( "tocineta express", 2500 );
        cebolla = new Ingrediente( "cebolla", 1000 );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", ajustado.getNombre( ), "El nombre debe ser el del producto base." );
    }

    @Test
    void testGetPrecioSinCambios( )
    {
        assertEquals( 14000, ajustado.getPrecio( ), "Sin ajustes el precio debe ser el del producto base." );
    }

    @Test
    void testGetPrecioConAgregados( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( tocineta );
        assertEquals( 19000, ajustado.getPrecio( ), "El precio debe sumar los ingredientes agregados." );
    }

    @Test
    void testGetPrecioMismoIngredienteDosVeces( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( queso );
        assertEquals( 19000, ajustado.getPrecio( ), "Un ingrediente agregado dos veces se cobra dos veces." );
    }

    @Test
    void testGetPrecioConEliminados( )
    {
        ajustado.eliminarIngrediente( cebolla );
        assertEquals( 14000, ajustado.getPrecio( ), "Quitar ingredientes no reduce el precio." );
    }

    @Test
    void testGenerarTextoFacturaSinCambios( )
    {
        String esperado = "corral\n            14000\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaConCambios( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( cebolla );
        String esperado = "corral\n" + "    +queso mozzarella                2500\n" + "    -cebolla\n" + "            16500\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }
}