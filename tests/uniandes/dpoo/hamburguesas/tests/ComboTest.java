package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private ArrayList<ProductoMenu> items;
    private Combo combo;

    @BeforeEach
    void setUp( )
    {
        items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "corral", 14000 ) );
        items.add( new ProductoMenu( "papas medianas", 5500 ) );
        items.add( new ProductoMenu( "gaseosa", 5000 ) );
        combo = new Combo( "combo corral", 0.1, items );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo corral", combo.getNombre( ), "El nombre del combo no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        // 14000 + 5500 + 5000 = 24500, con 10% de descuento = 22050
        assertEquals( 22050, combo.getPrecio( ), "El precio debe ser la suma de los productos menos el descuento." );
    }

    @Test
    void testGetPrecioSinDescuento( )
    {
        Combo sinDescuento = new Combo( "combo normal", 0, items );
        assertEquals( 24500, sinDescuento.getPrecio( ), "Con descuento 0 el precio debe ser la suma completa." );
    }

    @Test
    void testListaCopiada( )
    {
        items.add( new ProductoMenu( "papas grandes", 6900 ) );
        assertEquals( 22050, combo.getPrecio( ), "El combo no debe cambiar si se modifica la lista original." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n";
        assertEquals( esperado, combo.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }
}