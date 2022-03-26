
import java.io.*;
import java.util.ArrayList;

public class Catalogo implements Serializable{ //Definicion clase catalogo
    private String nombre; // Nombre del producto
    private float precio;  // Precio del producto
    private String descripcion;  // Descripccion del producto
    private int existencias;  // Numero de existencias del producto
    private String imagen;  // Nombre de la imagen correspondiente al prodcucto

    public Catalogo(String n, float p, String d, int e, String i){ // Constructor del producto
        nombre=n;
        precio=p;
        descripcion=d;
        existencias=e;
        imagen=i;
    }

    static ArrayList<Catalogo> leer(File f) { //Deserializa un fichero y devuelve una lista con los objetos(productos) leidos
        ArrayList<Catalogo> catalogo = new ArrayList<Catalogo>(); // Crea una lista del tipo Catalogo
        try {
            FileInputStream fis = new FileInputStream(f); //Crea un flujo FileInput para leer el archivo
            ObjectInputStream ois = new ObjectInputStream(fis);  //Crea un flujo ObjectInput para leer los objetos serializados del archivo
            catalogo = (ArrayList<Catalogo>)ois.readObject();  // Lee del flujo de datos creado el objeto serializado y le hace un casteo al tipo adecuado
            // Cerramos los flujos
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catalogo; //Regresamos la lista con los objetos deserializados
    }

    static void guardar(ArrayList<Catalogo> catalogo, File f) { //Serializa una lista de objetos en un fichero
        try {
            //Crea los flujos de escritura para el fichero y para los objetos
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //Serializa la lista dentro del fichero
            System.out.printf("\nActualizando catalogo\n ");
            oos.writeObject(catalogo);
            oos.flush();
            //Cerramos los flujos
            fos.close();
            oos.close();
            System.out.printf("\nCatalogo Actualizado\n ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Catalogo buscarProductoen(ArrayList<Catalogo> catalogo){ //Buscamos un producto en la lista y lo devuelve
        for(Catalogo c : catalogo){ //recorre la lista
            if (c.nombre.equalsIgnoreCase(this.nombre)){ //compara el nombre del objeto buscado con cada uno de los 
                return c; //Si los nombre coinciden, devuelve el objeto que coi
            }
        }
        return null; // Si no encuentra una coincidencia, devuelve null
    }

    public void addProducto(ArrayList<Catalogo> catalogo){ //Agrega un producto a la lista
        Catalogo aux = this.buscarProductoen(catalogo); //Busca el producto en la lista
        if (aux==null){ // Si no existe actualmente en la lista lo agrega
            catalogo.add(this);
        }
        else{ //Si existe en la lista, solamente actualiza las existencia
            aux.existencias=aux.existencias+this.existencias;
            catalogo.add(aux);
        }   
    }

    // Setters y getters de los atributos.

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
