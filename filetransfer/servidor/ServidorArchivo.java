import java.net.*;
import java.io.*;
public class ServidorArchivo{
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(7000);
            //iniciamos ciclo infinito para esperar una conexion
            for (;;) {
                Socket cl = s.accept();
                System.out.println("\nConexión establecida desde"+cl.getInetAddress()+":"+cl.getPort());
                //flujo de nivel de bits de entrada ligado al socket
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                //leemos los datos principales del archivo y creamos el flujo para escribir el archivo de salida
                byte[] b = new byte[1024];
                int nfiles = dis.readInt();
                for (int i = 0; i < nfiles; i++) {
                    String nombre = dis.readUTF();
                    System.out.println("\nRecibimos el archivo:"+nombre);
                    long tam = dis.readLong();
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                    //preparamos los datos para recibir los paquetes de datos del archivo
                    long recibidos=0;
                    int n, porcentaje;
                    while (recibidos<tam) {
                        n = dis.read(b);
                        dos.write(b,0,n);
                        dos.flush();
                        recibidos = recibidos + n;
                        porcentaje = (int)(recibidos*100/tam);
                        System.out.print("Recibido: "+porcentaje+"%\r");
                    }
                    System.out.print("\nArchivo "+nombre+" recibido.");
                    dos.close();
                //cerramos los flujos
                }
                dis.close();
                cl.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
