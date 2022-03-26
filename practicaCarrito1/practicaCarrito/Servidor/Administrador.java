
import java.io.*;
import java.util.ArrayList;

public class Administrador {
    
    public static void main(String[] args) {
        ArrayList<Catalogo> catalogo = new ArrayList<Catalogo>();
        File f = new File("catalogo.txt");

        catalogo.add( new Catalogo("Chocomilk", 13.0f, "Leche polvo", 12,"choco.jpg") );
        catalogo.add( new Catalogo("Nesquick", 12.0f, "Leche polvo", 11,"nqucik.jpg") );
        catalogo.add( new Catalogo("Maruchan", 5.50f, "Sopa instantanea", 2,"maruchan.jpg") );

        Catalogo.guardar(catalogo,f);


    }
}