// Bibliotecas necesarias
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import java.net.*;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;

public class ClienteCarrito {

    public void getCatalogo( Socket cl ) {

         

    }
    public static void main(String[] args) {

        try {
            
            // Se define el flujo de entrada para obtener los datos del servidor
            BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
            System.out.printf("Escriba la dirección del servidor: ");
            String host = br.readLine();
            System.out.printf("\n\nEscriba el puerto: ");
            int pto = Integer.parseInt( br.readLine() );

            Socket cl = new Socket(host, pto);

            // Se define un flujo de nivel de bits de entrada ligado al socket
            DataInputStream dis = new DataInputStream( cl.getInputStream() );

            // Se leen los datos principales del archivo y se crea un flujo
            byte[] b = new byte[1024];
            String nombre = dis.readUTF();      //Lee la entrada de datos y la pone en una variable con caracteres UTF
            System.out.println("Recibimos el catalogo: " + nombre);
            long tam = dis.readLong();          //Lee la entrada en tipo de dato 'long'
            DataOutputStream dos = new DataOutputStream( new FileOutputStream(nombre) );

            // Se preparan los datos para recibir los paquetes de datos del archivo
            
            long recibidos = 0;
            int n, porcentaje;

            // Se define un ciclo donde se estará recibiendo los datos que envía el cliente
            while( recibidos < tam ) {
                
                n = dis.read( b );              // Se lee la entrada en bytes
                dos.write(b, 0, n);             // Se escribe los datos de salida
                dos.flush();
                recibidos = recibidos + n;
                porcentaje = ( int )( recibidos * 100 / tam );
                System.out.println("\n\nArchivo recibido al " + porcentaje );

            }
            InputStream is = cl.getInputStream();
            String dir=System.getProperty("user.dir");
            File carpeta=new File(dir+"/img");
            carpeta.mkdir();
            System.out.println("\n\nArchivo recibido. ");
            int nfiles=dis.readInt();
            for (int i = 0; i < nfiles; i++) {
                String n_img=dis.readUTF();
                byte[] tamAr=new byte[4];
                is.read(tamAr);
                int im_size=ByteBuffer.wrap(tamAr).asIntBuffer().get();
                byte[] imgAr=new byte[im_size];
                is.read(imgAr);

                ByteArrayInputStream bais=new ByteArrayInputStream(imgAr);
                BufferedImage img= ImageIO.read(bais);
                File f = new File("img/"+n_img); 
                ImageIO.write(img, "jpg", f);


            }

            


            //Se cierran los flujos del socket y el resto del programa
            //bais.close();
            is.close();
            dos.close();
            dis.close();
            cl.close();

            //ArrayList<Catalogo> catalogo = new ArrayList<Catalogo>();
            File f = new File(nombre);
            ArrayList<Catalogo> catalogo = Catalogo.leer(f);
            // try{
            //     File f = new File(nombre);
            //     ArrayList<Catalogo> catalogo = Catalogo.leer(f);
            //     //catalogo = new Catalogo( "nombre", 1, "descricpcion", 0).leer(f);
            //     /* FileInputStream fis = new FileInputStream("ObjSerializado.txt");
            //     ObjectInputStream ois = new ObjectInputStream(fis);
            //     ObjSerializable obj = (ObjSerializable) ois.readObject();         //El método readObject() recupera el objeto
                
            //     System.out.println("Valores de objeto serializado\nNumero: " + obj.getNumero() + "\nVersion: " + obj.getVersion() + "\nNombre: " + obj.getNombre());
    
            //     ois.close();
            //     fis.close(); */
            // } catch(Exception e){
            //     e.printStackTrace();
            // }

            System.out.println("Elija el articulo que desee comprar (Los parentesis representan las existencias del articulo):\n");

            catalogo.forEach( articulo -> {

                System.out.println(articulo.getNombre() + " (" + articulo.getExistencias() + ")\t\t$" + articulo.getPrecio() );
                System.out.println("Descripcion: " + articulo.getDescripcion());

            });



        } catch (Exception e) {
            //TODO: handle exception
        }

    }
}