
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
            Catalogo aux = (Catalogo) ois.readObject();
            while (aux!=null) {
                catalogo.add(aux);
                aux = (Catalogo) ois.readObject();
            }
            ois.close();
            fis.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return catalogo;
    }

    static void guardar(ArrayList<Catalogo> catalogo, File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Catalogo c : catalogo) {
                oos.writeObject(c);
                oos.flush();
            }
            fos.close();
            oos.close();
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

    public String getImagen() {
        return imagen;
    }

}
