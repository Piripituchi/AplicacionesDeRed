import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;
public class ClienteArchivo{
    public static void main(String[] args) {
        try {
            //Flujo de entrada para los datos del servidor
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Escriba la dirección del servidor:");
            String host = br.readLine();
            System.out.printf("\nEscriba el puerto:");
            int pto = Integer.parseInt(br.readLine());
            // Se define el socket
            Socket cl = new Socket(host, pto);
            // JfileChooser() para elegir los archivos
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);
            // se obtienen los datos principales
            if (r==JFileChooser.APPROVE_OPTION){
                File[] f = jf.getSelectedFiles(); //Manejador
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                dos.writeInt(f.length);
                for (File file : f) {
                    String archivo = file.getAbsolutePath(); //Dirección
                    String nombre = file.getName(); //Nombre
                    long tam = file.length(); //Tamaño
                    // se definen los flujos de bytes para leer el archivo y para enviar datos
                    DataInputStream dis = new DataInputStream(new FileInputStream(archivo));
                    // se envian los datos generals del archivo 
                    dos.writeUTF(nombre);
                    dos.flush(); 
                    dos.writeLong(tam);
                    dos.flush();
                    //leemos los datos contenidos y los enviamos en paquetes de 1024 bytes
                    byte[] b = new byte[1024];
                    long enviados = 0;
                    int porcentaje, n;
                    while (enviados < tam){
                        n = dis.read(b);
                        dos.write(b,0,n);
                        dos.flush();
                        enviados = enviados+n;
                        porcentaje = (int)(enviados*100/tam);
                        System.out.print("Enviado: "+porcentaje+"%\r");
                    }//While
                    //cerramos los flujos, el socket y cerramos los parentesis
                    System.out.print("\n\nArchivo "+nombre+" enviado\n");  
                    dis.close();  
                } 
                dos.close();
                cl.close(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
