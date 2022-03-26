import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class UDPCliente {
    public final static int PUERTO = 7;
    private final static int LIMITE = 100;
    public static void main(String[] args) {
        boolean bandera=false;
        SocketAddress remoto = new InetSocketAddress("127.0.0.1",PUERTO); //Crea un socket de direccion 
        try {
            DatagramChannel canal = DatagramChannel.open(); //Abre un canal de datagrama
            canal.configureBlocking(false); //Establece el modo de no bloqueante
            canal.connect(remoto); //Conecta este canal con la direccion dada
            Selector selector = Selector.open();
            canal.register(selector,SelectionKey.OP_WRITE); //Registra el selector en modo escritura
            ByteBuffer buffer = ByteBuffer.allocateDirect(4);
            int n = 0;
            while (true) {
                selector.select(5000); //espera 5 segundos por la conexión
                Set sk = selector.selectedKeys(); //regresa las llaves selecionadas
                if (sk.isEmpty() && n == LIMITE || bandera) { //Si ya no hay llaves y se llego al limite o la bandera esta en True
                    canal.close(); //Se cierra el canal
                    break;
                } else {
                    Iterator it = sk.iterator();
                    while (it.hasNext()) { //Mientras exita una llave siguiente
                        SelectionKey key = (SelectionKey)it.next();
                        it.remove();//saca una llave del iterable
                        if (key.isWritable()) { //Si el canal de esta llave esta listo para escribir
                            buffer.clear(); //Limpia el buffer
                            buffer.putInt(n); //Pone el dato en el buffer
                            buffer.flip(); // Invierte el biffer
                            canal.write(buffer); // Escribe el buffer en el canal
                            System.out.println("Escribiendo el dato: "+n);
                            n++;
                            if (n==LIMITE) { //Cuando se escriban todos los datos
                                //todos los paquetes han sido escritos; 
                                buffer.clear(); //Limpia el bufer
                                //Envia 1000 para indicar que finaliza la coneccion
                                buffer.putInt(1000); 
                                buffer.flip();
                                canal.write(buffer);
                                bandera = true;
                                key.interestOps(SelectionKey.OP_READ);
                                break;
                            }//if
                        }//if
                    }//while
                }//else
            }//while
        } catch (Exception e) {
            System.err.println(e);
        }//catch
    }//main
}//class
