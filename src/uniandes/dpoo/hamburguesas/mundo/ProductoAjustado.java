package uniandes.dpoo.hamburguesas.mundo;

import java.util.ArrayList;

public class ProductoAjustado implements Producto
{
    private ProductoMenu productoBase;

    private ArrayList<Ingrediente> agregados;

    private ArrayList<Ingrediente> eliminados;

    public ProductoAjustado( ProductoMenu productoBase )
    {
        this.productoBase = productoBase;
        agregados = new ArrayList<Ingrediente>( );
        eliminados = new ArrayList<Ingrediente>( );
    }

    public void agregarIngrediente( Ingrediente ingrediente )
    {
        agregados.add( ingrediente );
    }

    public void eliminarIngrediente( Ingrediente ingrediente )
    {
        eliminados.add( ingrediente );
    }

    @Override
    public String getNombre( )
    {
        return productoBase.getNombre( );
    }

    @Override
    public int getPrecio( )
    {
        int precio = productoBase.getPrecio( );
        for( Ingrediente ing : agregados )
        {
            precio += ing.getCostoAdicional( );
        }
        return precio;
    }

    @Override
    public String generarTextoFactura( )
    {
        StringBuffer sb = new StringBuffer( );
        sb.append( productoBase.getNombre( ) + "\n" );
        for( Ingrediente ing : agregados )
        {
            sb.append( "    +" + ing.getNombre( ) );
            sb.append( "                " + ing.getCostoAdicional( ) + "\n" );
        }
        for( Ingrediente ing : eliminados )
        {
            sb.append( "    -" + ing.getNombre( ) + "\n" );
        }
        sb.append( "            " + getPrecio( ) + "\n" );
        return sb.toString( );
    }
}