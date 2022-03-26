package echoDG.Cliente;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.nio.ByteBuffer;
public class CHolaD {
    public static void main(String[] args){
        try{
            DatagramSocket cl = new DatagramSocket(); //Creamos un socket no orientado a conexion
            //                          ENVIO DE MENSAJE
            System.out.print("Cliente iniciado, escriba un mensaje de saludo:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String mensaje = br.readLine(); // leemos el mensaje a enviar
            byte[] b = mensaje.getBytes();
            //Datos generales del socket con el que nos deseamos conectar
            String dst = "127.0.0.1";
            int pto = 2000;
            byte[] len=ByteBuffer.allocate(4).putInt(b.length).array(); //Convertimos la longitud del mensaje a un arreglo de bytes
            DatagramPacket l = new DatagramPacket(len,len.length,InetAddress.getByName(dst),pto);
            cl.send(l); // Enviamos la longitud del mensaje
            int enviado=0;
            while (enviado<b.length) { // Mientras no hayan sido enviados todos los datos del mensaje, envia otro paquete
                byte[] aux=Arrays.copyOfRange(b, enviado, enviado+512); //Creamos un buffer de 512 Bytes a partir del mensaje
                DatagramPacket p = new DatagramPacket(aux,aux.length,InetAddress.getByName(dst),pto); //
                cl.send(p); // Enviamos el paquete
                enviado=enviado+512; // Aumentamos el contador para enviar el siguiente paquete de 512 Bytes   
            }
            //                          RECEPCION DE RESPUESTA
            DatagramPacket r = new DatagramPacket(new byte[2000],2000); //Creamos un datagrama para recibir paquetes
            cl.receive(r); // Recibimos la longitud del mensaje de respuesta
            System.out.println("\nRespuesta recibida desde"+r.getAddress()+":"+r.getPort());
            int len_res= ByteBuffer.wrap(r.getData()).getInt(); //Convertimos los datos recibidos (bytes[]) a un numero entero
            System.out.println("Se recibira la siguiente cantidad de datos:"+ String.valueOf(len_res)+"\n");
            int recibidos=0;
            while (recibidos<len_res) { // Mientras no se hayan recibido la totalidad de datos, se espera otro paquete
                cl.receive(r); // Recibimos un paquete
                System.out.println("Datagrama recibido desde"+r.getAddress()+":"+r.getPort());
                String msj = new String(r.getData(),0,r.getLength());// Convertimos los datos recibidos (bytes[]) a una cadena
                System.out.println("Con el mensaje:"+ msj+"\n");// Imprimimos el mensaje de respuesta
                recibidos=recibidos+r.getLength();//aumentamos el contador de datos recibidos
            }
            cl.close();//Cerramos el socket
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}

