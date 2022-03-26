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
                // se definen los flujos de bytes para leer la carpeta y enviar las imagenes
                File carpeta = new File("img");
                OutputStream os = cl.getOutputStream();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                String[] list_img = carpeta.list();
                dos.writeInt(list_img.length);
                for (String img : list_img) {
                    File aux=new File("img/"+img);
                    String nombre = aux.getName();
                    dos.writeUTF(nombre);
                    dos.flush();
                    BufferedImage imagen= ImageIO.read(aux);
                    ImageIO.write(imagen, "jpg", baos);
                    byte[] im_size=ByteBuffer.allocate(4).putInt(baos.size()).array();
                    os.write(im_size);
                    os.write(baos.toByteArray());
                    os.flush();
                }

                //cerramos los flujos, el socket y cerramos los parentesis
                System.out.print("\n\nCatalogo enviado\n");  
                os.close();
                baos.close();
                dis.close();
                fis.close();
                dos.close();
                cl.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
