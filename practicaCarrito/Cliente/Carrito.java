import java.util.ArrayList;

public class Carrito {
    
    private ArrayList<Catalogo> carrito;

    public Carrito() {
        carrito = new ArrayList<Catalogo>();
    }

    public void addArticulo( Catalogo c, int cant ) {

        boolean existe = false;
        int id = 0;
        for( int i = 0; i < carrito.size(); i++ ) {

            if( carrito.get(i).getNombre().equals( c.getNombre() ) ) {
                existe = true;
                id = i;
            }

        }

        if( existe )
            carrito.get( id ).setExistencias( carrito.get( id ).getExistencias() + cant );
        else {
            
            carrito.add( 
                new Catalogo(
                    c.getNombre(),
                    c.getPrecio(),
                    c.getDescripcion(),
                    cant,
                    c.getImagen()
                )
            );

        }

    }

    public void showCarrito() {

        for( int i = 0; i < carrito.size(); i++ ) {
                
            System.out.println( ( i + 1 ) + ". " + carrito.get(i).getNombre() + " (" + carrito.get(i).getExistencias() + ")\t\t$" + carrito.get(i).getPrecio() );
            System.out.println("Descripcion: " + carrito.get(i).getDescripcion() );

        }

    }

    public ArrayList<Catalogo> getCarrito() {
        return carrito;
    }
}
