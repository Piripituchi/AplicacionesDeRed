import java.net.*;
import java.nio.ByteBuffer;

import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
public class ServidorCarrito {
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(3016);
            //iniciamos ciclo infinito para esperar una conexion
            for (;;) {
                Socket cl = s.accept();
                System.out.println("\nConexión establecida desde"+cl.getInetAddress()+":"+cl.getPort());
                // se definen los flujos de bytes para leer el archivo y para enviar datos
                File f=new File("catalogo.txt");
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                FileInputStream fis = new FileInputStream(f);
                DataInputStream dis = new DataInputStream(fis);
                //
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
                    n = dis.read(b);
                    dos.write(b,0,n);
                    dos.flush();
                    enviados = enviados+n;
                    porcentaje = (int)(enviados*100/tam);
                    System.out.print("Enviado: "+porcentaje+"%\r");
                }//While

                dis.close();
                Thread.sleep(300);

                System.out.print("\n\nCatalogo enviado\n");  
                
                File carpeta = new File("img");
                String[] list_img = carpeta.list();
                dos.writeInt(list_img.length);
                for (String img : list_img) {
                    File aux=new File("img/"+img);
                    String archivo = aux.getAbsolutePath();
                    String nombre = aux.getName();
                    long tam_img = aux.length();
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
                    //cerramos los flujos, el socket y cerramos los parentesis
                    System.out.print("\n\nArchivo "+nombre+" enviado\n");  
                    dis_img.close(); 
                    Thread.sleep(300);
                }
                //cerramos los flujos, el socket y cerramos los parentesis
                
                
                fis.close();
                dos.close();
                cl.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
