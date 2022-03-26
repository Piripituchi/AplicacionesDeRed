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
            // 2) Se crea una instancia de la clase
            Bebida ba = new Bebida("Absolut",750,3.5f,true,"Vodka",84648);
            // 3) imprime los atributos
            System.out.printf("\n\nAtributos del objeto:");
            System.out.printf("\nNombre: "+ba.getnombre()+"\nCapacidad: "+ba.getcapacidad()+"\nPorcentaje de alcohol: "+ba.getp_alcohol()+"\nCon dosificador: "+ba.getc_dosificador()+"\nTipo: "+ba.gettipo()+"\nLote: "+ba.getlote()); 
            // 4) Serializa el objeto
            File f=new File("serializado.txt");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // 5) y 6) imprimir en consola los valores del objeto serializado y guardar el objeto
            oos.writeObject(ba);
            System.out.printf("\n\nAtributos del objeto serializado:");
            System.out.printf("\nNombre: "+ba.getnombre()+"\nCapacidad: "+ba.getcapacidad()+"\nPorcentaje de alcohol: "+ba.getp_alcohol()+"\nCon dosificador: "+ba.getc_dosificador()+"\nTipo: "+ba.gettipo()+"\nLote: "+ba.getlote()); 
            // 7) Envio del archivo
            // se definen los flujos de bytes para leer el archivo y para enviar datos
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            //
            // se envian los datos generals del archivo 
            dos.writeUTF(f.getName());
            dos.flush(); 
            dos.writeLong(f.length());
            dos.flush();
            //leemos los datos contenidos y los enviamos en paquetes de 1024 bytes
            byte[] b = new byte[1024];
            long enviados = 0;
            int porcentaje, n, tam=(int)f.length();
            while (enviados < tam){
                n = dis.read(b);
                dos.write(b,0,n);
                dos.flush();
                enviados = enviados+n;
                porcentaje = (int)(enviados*100/tam);
                System.out.print("Enviado: "+porcentaje+"%\r");
            }//While
            //cerramos los flujos, el socket y cerramos los parentesis
            System.out.print("\n\nArchivo "+f.getName()+" enviado\n");  
            dis.close();
            fis.close();
            dos.close();
            oos.close();
            fos.close();
            cl.close();
            ba=null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
