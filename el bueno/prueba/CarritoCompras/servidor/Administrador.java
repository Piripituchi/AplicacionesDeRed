
import java.io.*;
import java.util.ArrayList;

public class Administrador {
    
    public static void main(String[] args) {
        ArrayList<Catalogo> catalogo = new ArrayList<Catalogo>();
        File f = new File("catalogo.txt");
        Catalogo c1 = new Catalogo("Chocomilk", 13.0f, "Leche polvo", 12,"choco.jpg");
        Catalogo c2 = new Catalogo("Nesquick", 12.0f, "Leche polvo", 11,"nqucik.jpg");
        Catalogo c3 = new Catalogo("Maruchan", 5.50f, "Sopa instantanea", 2,"maruchan.jpg");
        c1.addProducto(catalogo);
        c2.addProducto(catalogo);
        c3.addProducto(catalogo);
        Catalogo.guardar(catalogo,f);


    }
}