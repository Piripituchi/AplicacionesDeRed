

public class hilo_par extends Thread{
    public hilo_par(String nombre){
        super(nombre);
    }

    public void run() {
        for (int i = 0; i <= 10; i=i+2) {
            System.out.println("n:\t"+i+" Impreso por: "+getName());
        }
        System.out.println("Termina thread "+getName());
    }
}
