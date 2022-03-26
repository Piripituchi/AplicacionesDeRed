
import java.io.*;
import java.util.ArrayList;

public class Catalogo implements Serializable{
    private String nombre;
    private float precio;
    private String descripcion;
    private int existencias;
    private String imagen;

    public Catalogo(String n, float p, String d, int e, String i){
        nombre=n;
        precio=p;
        descripcion=d;
        existencias=e;
        imagen=i;
    }

    static ArrayList<Catalogo> leer(File f) {
        ArrayList<Catalogo> catalogo = new ArrayList<Catalogo>();
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            catalogo = (ArrayList<Catalogo>)ois.readObject();
            
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catalogo;
    }

    static void guardar(ArrayList<Catalogo> catalogo, File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            System.out.printf("\nActualizando catalogo\n ");
            oos.writeObject(catalogo);
            oos.flush();
            
            fos.close();
            oos.close();
            System.out.printf("\nCatalogo Actualizado\n ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Catalogo buscarProductoen(ArrayList<Catalogo> catalogo){
        for(Catalogo c : catalogo){
            if (c.nombre.equalsIgnoreCase(this.nombre)){
                return c;
            }
        }
        return null;
    }

    public void addProducto(ArrayList<Catalogo> catalogo){
        Catalogo aux = this.buscarProductoen(catalogo);
        if (aux==null){
            //System.out.printf("\n"+aux.nombre+" nene: "+producto.nombre);
            catalogo.add(this);
        }
        else{
            aux.existencias=aux.existencias+this.existencias;
            catalogo.add(aux);
        }   
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias( int exs ) {
        existencias = exs;
    }

    public String getImagen() {
        return imagen;
    }

}
