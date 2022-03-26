// Bibliotecas necesarias
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import java.net.*;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;

public class ClienteCarrito {

    public static void main(String[] args) {

        try {
            
            // Se define el flujo de entrada para obtener los datos del servidor
            BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
            System.out.printf("Escriba la dirección del servidor: ");
            String host = br.readLine();
            System.out.printf("\n\nEscriba el puerto: ");
            int pto = Integer.parseInt( br.readLine() );

            Socket cl = new Socket(host, pto);

            // Se define un flujo de nivel de bits de entrada ligado al socket
            DataInputStream dis = new DataInputStream( cl.getInputStream() );

            // Se leen los datos principales del archivo y se crea un flujo
            byte[] b = new byte[1024];
            String nombre = dis.readUTF();      //Lee la entrada de datos y la pone en una variable con caracteres UTF
            System.out.println("Recibimos el catalogo: " + nombre);
            long tam = dis.readLong();          //Lee la entrada en tipo de dato 'long'
            DataOutputStream dos = new DataOutputStream( new FileOutputStream(nombre) );

            // Se preparan los datos para recibir los paquetes de datos del archivo
            
            long recibidos = 0;
            int n, porcentaje;

            // Se define un ciclo donde se estará recibiendo los datos que envía el cliente
            while( recibidos < tam ) {
                
                n = dis.read( b );              // Se lee la entrada en bytes
                dos.write(b, 0, n);             // Se escribe los datos de salida
                dos.flush();
                recibidos = recibidos + n;
                porcentaje = ( int )( recibidos * 100 / tam );

            }
            
            System.out.println("\n\nArchivo recibido. ");

            byte[] b_img = new byte[1024];
            String dir=System.getProperty("user.dir");
            File carpeta=new File(dir+"/img");
            carpeta.mkdir();
            int nfiles = dis.readInt();
            for (int i = 0; i < nfiles; i++) {
                String nombre_img = dis.readUTF();
                //System.out.println("\nRecibimos el archivo:"+nombre);
                long tam_img = dis.readLong();
                System.out.println(nombre+"  ");
                DataOutputStream dos_img = new DataOutputStream(new FileOutputStream("img/"+nombre_img));
                //preparamos los datos para recibir los paquetes de datos del archivo
                recibidos=0;
                while (recibidos<tam_img) {
                    n = dis.read(b_img);
                    dos_img.write(b_img,0,n);
                    dos_img.flush();
                    recibidos = recibidos + n;
                    porcentaje = (int)(recibidos*100/tam_img);
                    System.out.print("Recibido: "+porcentaje+"%\r");
                }
                System.out.print("\nArchivo "+nombre+" recibido.");
                dos_img.close();
            }

            //Se cierran los flujos del socket y el resto del programa
            dos.close();
            dis.close();

            ArrayList<Catalogo> catalogo = new ArrayList<Catalogo>();

            
            boolean clienteEnTienda = true;
            
            while( clienteEnTienda ) {

                try{
                    File f = new File("catalogo.txt");
    
                    catalogo = Catalogo.leer(f);
                    /* FileInputStream fis = new FileInputStream("ObjSerializado.txt");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ObjSerializable obj = (ObjSerializable) ois.readObject();         //El método readObject() recupera el objeto
                    
                    System.out.println("Valores de objeto serializado\nNumero: " + obj.getNumero() + "\nVersion: " + obj.getVersion() + "\nNombre: " + obj.getNombre());
        
                    ois.close();
                    fis.close(); */
                } catch(Exception e){
                    e.printStackTrace();
                }

                System.out.println( "Elija el articulo que desee comprar (Los parentesis representan las existencias del articulo):\n" );
                
                for( int i = 0; i < catalogo.size(); i++ ) {
                    
                    System.out.println( ( i + 1 ) + ". " + catalogo.get(i).getNombre() + " (" + catalogo.get(i).getExistencias() + ")\t\t$" + catalogo.get(i).getPrecio() );
                    System.out.println("Descripcion: " + catalogo.get(i).getDescripcion() );
    
                }
    
                /* AÑADIR ARTICULOS AL CARRITO */
    
                Carrito carrito = new Carrito();
                int totalArtículos = 0;
                boolean condicionAddCarrito = true;
                while(condicionAddCarrito) {
                    
                    System.out.println("\n" + totalArtículos + " artículos en el carrito");
                    System.out.println(
                        "\n\nSeleccione un artículo o escriba 'fin' para realizar la compra o volver a ver los artículos." +
                        "\nEscriba 'e' para editar los artículos en el carrito.\n" + 
                        "\nEscripa 'salir' para salir de la tienda\n"
                    );
                    boolean cantCorrecta = false;
                    String idArticulo = br.readLine();
                    int seleccion = idArticulo.matches("\\d+") ? Integer.parseInt( idArticulo ) : -5;
                    if( idArticulo.equals("fin") ) {
                        break;
                    } else if( idArticulo.equals("salir") ) {
                        clienteEnTienda = false;
                        break;
                    } else if( seleccion <= 0) {
                        System.out.println("Seleccione un identificador de artíulo válido");
                        cantCorrecta = true;
                    } else if( seleccion == -5 ) {
                        System.out.println("Seleccione una opción correcta");
                    } else if( idArticulo.equals("e") ) {

                        if( carrito.getCarrito().size() < 1 ) {
                            System.out.println("\nPara editar los articulos del carrito primero debe tener alguno dentro del carrito.");
                        } else {

                            System.out.println("\nArticulos del carrito\n");
                            carrito.showCarrito();
                            System.out.println("\nElija el identificador del artículo que desea editar o precione 'c' para cancelar: ");
                            

                        }

                    } else {
    
                        while( !cantCorrecta ) {
                            
                            System.out.println("Indique la cantidad que desea de este artículo o presione 'c' para cancelar: ");
                            String str = br.readLine();
                            int cant = str.matches("\\d+") ? Integer.parseInt( str ) : 0;
                            if( !str.matches("\\d+") )
                                break;
                            if( cant >= catalogo.get( seleccion - 1).getExistencias() ) {
                                System.out.println("No hay existencias suficientes de este artículo para la cantidad solicitada, vuelva a intentarlo");
                            } else if( cant <= 0 ) {
                                System.out.println("Seleccione una cantidad mayor a 0");
                            } else {
                                cantCorrecta = true;
                                carrito.addArticulo( catalogo.get( seleccion - 1 ), cant );
                                catalogo.get( seleccion - 1 ).setExistencias( catalogo.get( seleccion - 1 ).getExistencias() - cant );
                                totalArtículos += cant;
                                System.out.println("El artículo fue añadido correctamente al carrito");
                            }
                            
                        }
                        
                    }
    
    
                }

                /* REALIZAR LA COMPRA */

                if( clienteEnTienda && ( carrito.getCarrito().size() > 0 ) ) {

                    System.out.println("\nArticulos en el carrito de compras:\n\n");
                    carrito.showCarrito();
                    System.out.println("\n\n");
                    System.out.println("¿Desea finalizar la compra? s/n: ");

                    boolean realizarCompra = br.readLine().equals("s") ? true : false;

                    if( realizarCompra ) {
                        
                    }

                }

            }
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}