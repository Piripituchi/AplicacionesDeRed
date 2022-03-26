
import java.io.*;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public class Administrador { //Definicion de la clase Administrador
    
    public static void main(String[] args) { //Este modulo carga y modifica el catalogo de productos
        try {
            BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) ); //Creamos el flujo para leer del teclado las opciones 
            ArrayList<Catalogo> catalogo = new ArrayList<Catalogo>(); // Creamos una lista para cargar el catalogo
            File f = new File("catalogo.txt"); // Leemos el fichero que contiene el catalogo
            catalogo=Catalogo.leer(f); // Deserializamos el catalogo
            boolean salir = false; // Boolean para detener la ejecucion del menu
            int opcion; // Entero para leer la opcion elegida
            while (!salir) { // Mientra no se escoja la opcion salir, se continua ejecutando el menu
                //Muestra el catalogo
                System.out.println( "\nCatalogo:\n" );
                for( int i = 0; i < catalogo.size(); i++ ) {
                    
                    System.out.println( ( i + 1 ) + ". " + catalogo.get(i).getNombre() + " (" + catalogo.get(i).getExistencias() + ")\t\t$" + catalogo.get(i).getPrecio() );
                    System.out.println("Descripcion: " + catalogo.get(i).getDescripcion() );
    
                }
                // Opciones del menu
                System.out.println("\n1. Agregar nuevo producto");
                System.out.println("2. Eliminar producto");
                System.out.println("3. Actualizar existencias");
                System.out.println("4. Salir");
                // Lectura de la opcion
                System.out.printf("Escribe una de las opciones: ");
                opcion=Integer.parseInt( br.readLine() );
                switch (opcion) {
                    case 1: //Agregar un producto a la lista
                        // Lectura de los atributos del nuevo producto desde el teclado
                        System.out.printf("Ingrese el nombre: ");
                        String nombre=br.readLine();
                        System.out.printf("Ingrese el precio unitario: ");
                        Float precio=Float.parseFloat(br.readLine());
                        System.out.printf("Ingrese una descripcion: ");
                        String descripcion=br.readLine();
                        System.out.printf("Ingrese la cantidad en existencia: ");
                        int canti=Integer.parseInt(br.readLine());
                        // Seleccion de imagen que representara al producto
                        JFileChooser jf=new JFileChooser(); //Abre el selector de archivos
                        int r = jf.showOpenDialog(null);
                        if (r==JFileChooser.APPROVE_OPTION) { // Si se selecciono un archivo
                            File selected = jf.getSelectedFile(); //Obtenemos la imagen seleccionada
                            String nombre_archivo=selected.getName(); //Obetenmos el nombre de la imagen
                            File img=new File("img/"+nombre_archivo); // Creamos un nuevo archivo con el nombre de la imagen seleccionada
                            // Creamos los flujos para leer la imagen seleccionada y escribirla dentro de la carpeta de imagenes del catalogo
                            FileOutputStream fos = new FileOutputStream(img);
                            FileInputStream fis= new FileInputStream(selected);
                            // Definimos el buffer de bytes para leer la imagen con el tamaño de la imagen
                            byte[] b = new byte[(int)selected.length()];
                            int len;
                            len=fis.read(b); //Leemos y obtenemos el total de numero de bytes leidos
                            //Escribimos en el nuevo fichero
                            fos.write(b, 0, len); 
                            fos.flush();
                            //Añadimos a la lista el nuevo objeto
                            catalogo.add(new Catalogo(nombre,precio,descripcion,canti,nombre_archivo));
                            //Cerramos los flujos
                            fos.close();
                            fis.close();
                        }
                        break;
                    case 2: //Eliminar producto
                        //Lee el nombre del producto a eliminar
                        System.out.printf("Ingrese el nombre: "); 
                        String eliminar=br.readLine();
                        for (Catalogo c:catalogo) {
                            // Recorre el catalogo
                            if (c.getNombre().equalsIgnoreCase(eliminar)) { //Si encuentra una coincidencia
                                //Elimina la imagen correspondiente al producto
                                File elim_img= new File("img/"+c.getImagen()); 
                                elim_img.delete();
                                //Elimina el producto del catalogo
                                catalogo.remove(c);
                                break; //Termina
                            }
                        } 
                        // Si no fue encontrado, lo indica y termina
                        System.out.println("\nNo se encontro el elemento en el catalogo\n");
                        break;
                    case 3: //Actualiza existencias
                        // Lee el nombre del producto a modificar
                        System.out.printf("Ingrese el nombre: ");
                        String mod=br.readLine();
                        for (Catalogo c:catalogo) {
                            // Recorre el catalogo
                            if (c.getNombre().equalsIgnoreCase(mod)) { //Si encuentra una coincidencia
                                // Lee la nueva cantidad en exitencia del producto
                                System.out.printf("Ingrese la nueva cantidad en existencia: ");
                                int actualizar=Integer.parseInt(br.readLine());
                                // Actualiza la cantidad en existencia del producto
                                c.setExistencias(actualizar);
                                break; // Termina
                            }
                        }
                        // Si no fue encontrado, lo indica y termina
                        System.out.println("\nNo se encontro el elemento en el catalogo\n");
                        break;
                    case 4: //Salir
                        //cambia el estado de salir a true y el ciclo while termina
                        salir=true;
                        
                    default: // Cualquier otra opcion
                        System.out.printf("Ingrese una de las opciones ");
                        break;
                }
            }
            // Guarda el estado del catalago
            Catalogo.guardar(catalogo,f);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}