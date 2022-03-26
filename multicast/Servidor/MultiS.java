import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public class MultiS {
    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException{  //Muestra la informacion de la interfaz
        System.out.printf("Interfaz: %s\n", netint.getDisplayName()); //Muestra el nombre de la interfaz network como una cadena
        System.out.printf("Nombre: %s\n", netint.getName()); //Muestra el nombre de la interfaz en formato  original
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("InetAddress: %s\n", inetAddress); //Muestra todas las direcciones Inet de la interfaz
        }
        System.out.printf("\n");
    }
    public static void main(String[] args) {
        try {
            int pto=2000;
            Enumeration<NetworkInterface> nets =NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                displayInterfaceInformation(netint);//Muestra todas la interfaces de el equipo
            }
            NetworkInterface ni = NetworkInterface.getByName("eth3");//Busca la interfaz "eth3" y regresa el nombre 
            InetSocketAddress dir = new InetSocketAddress(pto); //crea un socket
            DatagramChannel s =DatagramChannel.open(StandardProtocolFamily.INET); //Abre un canal de datagrama del tipo INET
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);//Establece la opcione de reusar direcciones
            s.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni); //Establece la opcion de interfaz IPv6 multicast
            InetAddress group = InetAddress.getByName("230.0.0.1");
            s.join(group, ni);  //Crea el grupo multicast en la direccion dada
            s.configureBlocking(false); //Establece el modo de no bloqueante
            s.socket().bind(dir); // Enlaza el socket al canal de datagrama
            Selector sel = Selector.open();
            s.register(sel, SelectionKey.OP_READ); //Registra el selector en modo lectura 
            ByteBuffer b = ByteBuffer.allocate(4);
            System.out.println("Servidor listo.. Esperando datagramas...");
            //int n=0
            while (true) {
                sel.select(); //Selecciona una llave
                Iterator<SelectionKey>it = sel.selectedKeys().iterator();
                while (it.hasNext()) { //Mientras exita una llave siguiente
                    SelectionKey k = (SelectionKey)it.next();
                    it.remove(); //saca una llave del iterable
                    if (k.isReadable()) { //Si el canal de esta llave esta listo para leer
                        DatagramChannel ch = (DatagramChannel)k.channel(); //regres el canal de la llave
                        b.clear(); //limpia el buffer
                        SocketAddress emisor = ch.receive(b); //Recive el datagrama y lo escribe en el buffer
                        b.flip(); // Invierte el buffer
                        InetSocketAddress d = (InetSocketAddress)emisor; //Castea la direccion del socket emisor para obtener la direccionn Inet
                        System.out.println("Datagrama recibido desde "+ d.getAddress()+":"+d.getPort());
                        System.out.println("Dato: "+b.getInt());
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
