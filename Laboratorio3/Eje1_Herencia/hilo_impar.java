

public class hilo_impar extends Thread{
    public hilo_impar(String nombre){
        super(nombre);
    }

    public void run() {
        for (int i = 1; i < 10; i=i+2) {
            System.out.println("n:\t"+i+" Impreso por: "+getName());
        }
        System.out.println("Termina thread "+getName());
    }
}
