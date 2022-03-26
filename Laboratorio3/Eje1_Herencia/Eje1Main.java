

public class Eje1Main {
    public static void main(String[] args) {
        new hilo_par("Par 1").start();
        new hilo_impar("Impar 1").start();
        new hilo_par("Par 2").start();
        new hilo_impar("Impar 2").start();
        System.out.println("Termina thread main");
    }
}
