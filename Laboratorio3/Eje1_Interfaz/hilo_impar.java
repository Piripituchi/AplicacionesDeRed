

public class hilo_impar implements Runnable{

    public void run() {
        for (int i = 1; i < 10; i=i+2) {
            System.out.println("n:\t"+i+" Impreso por: "+Thread.currentThread().getName());
        }
        System.out.println("Termina thread "+Thread.currentThread().getName());
    }
}
