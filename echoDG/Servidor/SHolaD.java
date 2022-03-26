package echoDG.Servidor;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
public class SHolaD {
    public static void main(String[] args){
        try{
            DatagramSocket s = new DatagramSocket(2000); //Creamos un socket en el puerto 2000
            System.out.println("Servidor iniciado, esperando cliente");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[2000],2000); //Creamos un datagrama para enviar paquetes
                // NOTA: El tamaño con el que definimos este paquetes es irrelevante, ya que el tamaño real de los paquetes sera de 512 Bytes
                //                          RECEPCION DE MENSAJE
                s.receive(p);  // Recibe la longitud del datagrama, para saber cuantas veces esperar
                System.out.println("\nDatagrama recibido desde"+p.getAddress()+":"+p.getPort());
                int len= ByteBuffer.wrap(p.getData()).getInt(); //Convertimos los datos recibidos (bytes[]) a un numero entero
                System.out.println("Se recibira la siguiente cantidad de datos:"+ String.valueOf(len)+"\n");
                int recibidos=0;
                while (recibidos<len) { //Mientras no hayan recibido todos los datos, se seguira esperando datos
                    s.receive(p); // Recibimos un paquete de datos
                    System.out.println("Datagrama recibido desde"+p.getAddress()+":"+p.getPort());
                    String msj = new String(p.getData(),0,p.getLength()); // Convertimos los datos recibidos (bytes[]) a una cadena
                    System.out.println("Con el mensaje:"+ msj+"\n"); // imprimimos el mensaje recibido
                    recibidos=recibidos+p.getLength(); //aumentamos el contador de datos recibidos
                }
                //                          ENVIO DE RESPUESTA
                System.out.print("Escriba un mensaje de respuesta:"); 
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
                String respuesta = br.readLine(); //Leemos la respuesta
                byte[] b = respuesta.getBytes();
                byte[] len_res=ByteBuffer.allocate(4).putInt(b.length).array(); //Obtenemos la longitud de el mensaje de respuesta
                DatagramPacket l = new DatagramPacket(len_res,len_res.length,p.getAddress(),p.getPort());
                s.send(l); // Enviamos la longitud del mensaje de respuesta
                int enviado=0;
                while (enviado<b.length) { // Mientras no hayan sido enviados todos los datos del mensaje, envia otro paquete
                    byte[] aux=Arrays.copyOfRange(b, enviado, enviado+512); // Creamos un buffer de 512 Bytes a partir del mensaje de respuesta
                    DatagramPacket r = new DatagramPacket(aux,aux.length,p.getAddress(),p.getPort()); 
                    s.send(r); // Enviamos el paquete
                    enviado=enviado+512; // Aumentamos el contador para enviar los siguientes 512 Bytes   
                }
            }//for
            //s.close();
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
