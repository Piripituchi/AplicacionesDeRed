import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;


public class UDPServidor {
    public final static int PUERTO = 7;
    public final static int TAM_MAXIMO = 65507; 
    public static void main(String[] args) {
        int port = PUERTO;
        try {
            DatagramChannel canal = DatagramChannel.open(); //Abre un canal de datagrama
            canal.configureBlocking(false); //Establece el modo de no bloqueante
            DatagramSocket socket = canal.socket(); //Regresa un socket de datagrama asociado al canal
            SocketAddress dir = new InetSocketAddress(port); //Crea un socket en el puerto dado
            socket.bind(dir); //Asocia el socket de direccion con el socket de datagrama
            Selector selector = Selector.open();
            canal.register(selector,SelectionKey.OP_READ);//Registra el selector en modo escritura
            ByteBuffer buffer = ByteBuffer.allocateDirect(TAM_MAXIMO);
            while (true) {
                selector.select(5000);  //Selecciona una llave esperando solo 5000 milisegundos
                Set sk = selector.selectedKeys(); //regresa las llaves selecionadas
                Iterator it = sk.iterator();
                while (it.hasNext()) {//Mientras exita una llave siguiente
                    SelectionKey key = (SelectionKey)it.next();
                    it.remove();//saca una llave del iterable
                    if (key.isReadable()) {//Si el canal de esta llave esta listo para leer
                        buffer.clear(); //limpia el bufefr
                        SocketAddress client = canal.receive(buffer); //Recive el datagrama y lo escribe en el buffer
                        buffer.flip(); //invierte el buffer
                        int eco = buffer.getInt(); //castea el dato del buffear a entero
                        if (eco==1000) { //Todos los datos fueron escritos
                            canal.close(); //Termina
                            System.exit(0);
                        } else {
                            System.out.println("Dato leido: "+eco); //Escribe el dato recibido
                            buffer.flip(); //Invierte el buffer
                            canal.send(buffer,client); //Envia el datagrama
                        }//else
                    }//f
                }//while2
            }//while
        } catch (Exception e) {
            System.err.println(e);
        }//catch
    }//main
}//class
