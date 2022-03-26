package eco;
import java.net.*;
import java.io.*;
public class SHola {
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Esperando cliente ...");
            for(;;){
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde" + cl.getInetAddress()+":"+cl.getPort());
                String mensaje ="Jesus Eduardo Angeles Hernandez";
                PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cl.getOutputStream())),true);
                pw.println(mensaje);
                BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                String eco = br.readLine();
                System.out.println("Se recibio una respuesta de" + cl.getInetAddress()+":"+cl.getPort());
                System.out.println("Respuesta:"+eco);
                pw.flush();
                pw.close();
                br.close();
                cl.close();
            }//for
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
