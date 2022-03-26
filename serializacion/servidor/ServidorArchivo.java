import java.net.*;
import java.io.*;
public class ServidorArchivo{
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(7171);
            //iniciamos ciclo infinito para esperar una conexion
            for (;;) {
                Socket cl = s.accept();
                System.out.println("\nConexión establecida desde"+cl.getInetAddress()+":"+cl.getPort());
                // 1) recibe el archivo que contiene el objeto serializado
                //flujo de nivel de bits de entrada ligado al socket
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                //leemos los datos principales del archivo y creamos el flujo para escribir el archivo de salida
                byte[] b = new byte[1024];
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
                // 2) Deserializamos
                FileInputStream fis = new FileInputStream(nombre);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Bebida ba=(Bebida) ois.readObject();
                // 3) Imprimimos los valores del objeto
                System.out.printf("\n\nAtributos del objeto recibido:");
                System.out.printf("\nNombre: "+ba.getnombre()+"\nCapacidad: "+ba.getcapacidad()+"\nPorcentaje de alcohol: "+ba.getp_alcohol()+"\nCon dosificador: "+ba.getc_dosificador()+"\nTipo: "+ba.gettipo()+"\nLote: "+ba.getlote()); 
                //cerramos los flujos
                ois.close();
                fis.close();
                dos.close();
                dis.close();
                cl.close();
                ba=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
