import java.net.*;
import java.io.*;

public class CPrimD {
    public static void main(String[] args) {
        try {
            int pto = 2000;
            InetAddress dst = InetAddress.getByName("127.0.0.1");
            //creamos los flujos de datos necesarios para enviar el datagrama
            DatagramSocket cl = new DatagramSocket(); //Creamos un socket de datagrama
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(4); //Escribimos un 4 en el flujo de datos
            dos.flush(); //Forzamos a que el dato se escriba en flujo de bytes
            dos.writeFloat(4.1f); //Escribimos un 4.1 en el flujo de datos
            dos.flush(); //Forzamos a que el dato se escriba en flujo de bytes
            dos.writeLong(72);//Escribimos un 72 en el flujo de datos
            dos.flush(); //Forzamos a que el dato se escriba en flujo de bytes
            byte[] b = baos.toByteArray(); //Convierte el flujo a un bytearray
            DatagramPacket p = new DatagramPacket(b,b.length,dst,pto); //Crea el paquete de datagrama
            cl.send(p); //Envia el paquete de datagrama por el socket
            cl.close(); //Cierra el socket
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main
}
