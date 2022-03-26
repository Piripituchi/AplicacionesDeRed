import java.net.*;
import java.io.*;
public class ServidorCarrito { //Definicion de clase ServidorCarrito
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(3016);
            //iniciamos ciclo infinito para esperar una conexion
            for (;;) {
                Socket cl = s.accept();
                System.out.println("\nConexión establecida desde"+cl.getInetAddress()+":"+cl.getPort());
                // se definen los flujos de bytes para leer el archivo y para enviar datos
                File f=new File("catalogo.txt"); //Leemos el archivo donde se encuentra el catalogo serializado
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                FileInputStream fis = new FileInputStream(f);
                DataInputStream dis_file = new DataInputStream(fis);
                // se envian los datos generals del archivo 
                dos.writeUTF(f.getName());
                dos.flush(); 
                dos.writeLong(f.length());
                dos.flush();
                //leemos los datos contenidos y los enviamos en paquetes de 1024 bytes
                byte[] b = new byte[1024];
                long enviados = 0;
                int porcentaje, n, tam=(int)f.length();
                while (enviados < tam){
                    n = dis_file.read(b);
                    dos.write(b,0,n);
                    dos.flush();
                    enviados = enviados+n;
                    porcentaje = (int)(enviados*100/tam);
                    System.out.print("Enviado: "+porcentaje+"%\r");
                }//While
                dis_file.close(); //Cerramos el flujo que mediante el cual leemos el archivo
                System.out.print("\n\nCatalogo enviado\n");  
                // Envio de la carpeta de imagenes
                File carpeta = new File("img");
                String[] list_img = carpeta.list(); //Listamos las imagenes contenidas en la carpeta
                dos.writeInt(list_img.length); //Enviamos el numero de elementos de la carpeta
                for (String img : list_img) { // Recorremos la lista de imagenes
                    File aux=new File("img/"+img); // Obetenmos la imagen
                    String archivo = aux.getAbsolutePath(); // Obtenemos la ruta del archivo
                    String nombre = aux.getName(); //Obtenemos el nombre del archivo
                    long tam_img = aux.length(); // Obtenemos la longitud
                    System.out.println(archivo+"  "+nombre+"  ");
                    // se definen los flujos de bytes para leer el archivo y para enviar datos
                    DataInputStream dis_img = new DataInputStream(new FileInputStream(archivo));
                    // se envian los datos generals del archivo 
                    dos.writeUTF(nombre);
                    dos.flush(); 
                    dos.writeLong(tam_img);
                    dos.flush();
                    byte[] b_img = new byte[1024];
                    enviados=0;
                    while (enviados < tam_img){
                        n = dis_img.read(b_img);
                        dos.write(b_img,0,n);
                        dos.flush();
                        enviados = enviados+n;
                        porcentaje = (int)(enviados*100/tam_img);
                        System.out.print("Enviado: "+porcentaje+"%\r");
                    }//While
                    //cerramos los flujos y cerramos los parentesis
                    System.out.print("\n\nArchivo "+nombre+" enviado\n");  
                    dis_img.close(); 
                    Thread.sleep(300); //Esperamos 300 mills para que el cliente este listo para recibir la siguiente imagen
                }
                //cerramos los flujos, el socket y cerramos los parentesis
                fis.close();
                dos.close();
                //Esperamos la respuesta del cliente.
                DataInputStream dis_resp = new DataInputStream( cl.getInputStream() );
                byte[] b_new = new byte[1024];
                long tam_new = dis_resp.readLong();          //Lee la entrada en tipo de dato 'long'
                DataOutputStream dos_new = new DataOutputStream( new FileOutputStream(f) );
                long recibidos = 0;
                int n_recib;

                // Se define un ciclo donde se estará recibiendo los datos que envía el cliente
                while( recibidos < tam_new ) {
                
                    n_recib = dis_resp.read( b_new );              // Se lee la entrada en bytes
                    dos_new.write(b_new, 0, n_recib);             // Se escribe los datos de salida
                    dos_new.flush();
                    recibidos = recibidos + n_recib;
                    //porcentaje_recib = ( int )( recibidos * 100 / tam_new );

                }
            
                System.out.println("\n\nArchivo recibido. ");
                //Cerramos los flujos
                dos_new.close();
                dis_resp.close();
                //Cerramos el socket
                cl.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
