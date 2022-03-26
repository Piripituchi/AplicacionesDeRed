package eco;
import java.net.*;
import java.io.*;
public class Cliente {
    public static void main(String[] args) {
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Escriba la dirección del servidor: ");
            String host = br1.readLine();
            System.out.printf("\n\nEscriba el puerto:");
            int pto = Integer.parseInt(br1.readLine());
            Socket cl = new Socket(host,pto);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            String mensaje = br2.readLine();
            System.out.println("Recibimos un mensaje desde el servidor");
            System.out.println("Mensaje:"+mensaje);
            System.out.println("Reenviando mensaje al servidor");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cl.getOutputStream())), true);
            pw.println(mensaje);
            pw.flush();
            pw.close();
            br1.close();
            br2.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
