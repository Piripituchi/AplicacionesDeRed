import java.net.*;
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
                // String[] list_img = carpeta.list();
                // String dir=carpeta.getPath();
                dos.writeInt(carpeta.listFiles().length);
                dos.flush();
                System.out.println("longitud: "+carpeta.listFiles().length);
                for (File img : carpeta.listFiles()) {
                    String archivo = img.getAbsolutePath(); //Dirección
                    String nombre_img = img.getName(); //Nombre
                    long tam_img = img.length(); //Tamaño
                    // se definen los flujos de bytes para leer el archivo y para enviar datos
                    DataInputStream dis_img = new DataInputStream(new FileInputStream(img));
                    // se envian los datos generals del archivo 
                    dos.writeUTF(nombre_img);
                    //System.out.println(nombre_img);
                    dos.flush(); 
                    dos.writeLong(tam_img);
                    dos.flush();
                    //leemos los datos contenidos y los enviamos en paquetes de 1024 bytes
                    byte[] b_img = new byte[(int)img.length()];
                    dis_img.read(b_img);
                    dos.write(b_img,0,b_img.length);
                    dos.flush();
                    // long enviados_img = 0;
                    // int porcentaje_img, n_img;
                    // while (enviados_img < tam_img){
                    //     n_img = dis_img.read(b_img,0,b_img.length);
                    //     dos.write(b_img,0,n_img);
                    //     dos.flush();
                    //     enviados_img = enviados_img+n_img;
                    //     porcentaje_img = (int)(enviados_img*100/tam_img);
                    //     System.out.print("Enviado: "+porcentaje_img+"%\r");
                    // }//While

                    //cerramos los flujos, el socket y cerramos los parentesis
                    System.out.print("\n\nArchivo "+nombre_img+" enviado\n");  
                    dis_img.close();  
                }
                //cerramos los flujos, el socket y cerramos los parentesis
                System.out.print("\n\nCatalogo enviado\n");  
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
