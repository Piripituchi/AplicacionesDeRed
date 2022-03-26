

public class hilo_par implements Runnable{

    public void run() {
        for (int i = 0; i <= 10; i=i+2) {
            System.out.println("n:\t"+i+" Impreso por: "+Thread.currentThread().getName());
        }
        System.out.println("Termina thread "+Thread.currentThread().getName());
    }
}
