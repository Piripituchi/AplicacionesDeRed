import java.net.*;
import java.io.*;

public class SPrimD {
    public static void main(String[] args) {
        try {
            DatagramSocket s = new DatagramSocket(2000); //Crea un socket de datagram en el puerto 2000
            System.out.println("Servidor iniciado, esperando cliente");
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[2000],2000); //Creamos un datagrama para enviar paquetes de tamaño 2000 bytes
                s.receive(p); //Recibimos el paquete de datagrama
                System.out.println("Datagrama recibido desde"+p.getAddress()+":"+p.getPort());
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData())); //Creamos un flujo para leer el datagrama
                //Casteamos leemos los datos del datagrama y casteamos
                int x = dis.readInt();
                float f = dis.readFloat();
                long z = dis.readLong();
                System.out.println("\n\nEntero:"+ x + " Flotante:"+f+" Entero largo:"+z);
            }//for
            //s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main
}