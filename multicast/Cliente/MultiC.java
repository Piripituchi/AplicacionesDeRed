import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public class MultiC {
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
        int pto=2000;
        String hhost="230.0.0.1";
        SocketAddress remote=null;
        try {
            try {
                remote = new InetSocketAddress(hhost, pto); //Crea un socket
            } catch (Exception e) {
                e.printStackTrace();
            }
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                displayInterfaceInformation(netint); //Muestra todas la interfaces de el equipo
            }
            NetworkInterface ni = NetworkInterface.getByName("eth3"); //Busca la interfaz "eth3" y regresa el nombre 
            DatagramChannel cl = DatagramChannel.open(StandardProtocolFamily.INET); //Abre un canal de datagrama del tipo INET
            cl.setOption(StandardSocketOptions.SO_REUSEADDR, true); //Establece la opcione de reusar direcciones
            cl.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni); //Establece la opcion de interfaz IPv6 multicast
            cl.configureBlocking(false); //Establece el modo de no bloqueante
            Selector sel = Selector.open();
            cl.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE); //Registra el selector en modo lectura o escritura
            InetAddress group = InetAddress.getByName("230.0.0.1");
            cl.join(group, ni); //Crea el grupo multicast en la direccion dada
            ByteBuffer b = ByteBuffer.allocate(4);
            int n=0;
            while (n<100) {
                sel.select(); //Selecciona una llave
                Iterator<SelectionKey>it = sel.selectedKeys().iterator();
                while (it.hasNext()) { //Mientras exita una llave siguiente
                    SelectionKey k = (SelectionKey)it.next();
                    it.remove(); //saca una llave del iterable
                    if (k.isWritable()) { //Si el canal de esta llave esta listo para escribir
                        DatagramChannel ch = (DatagramChannel)k.channel();//regres el canal de la llave
                        b.clear(); //Limpia el buffer
                        b.putInt(n++); //Escribe en el buffer
                        b.flip(); //Invierte el buffer
                        ch.send(b, remote); //Envia el buffer atraves del canal
                        continue;
                    } 
                }
            }
            cl.close();
            System.out.println("Termina envio de datagramas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
