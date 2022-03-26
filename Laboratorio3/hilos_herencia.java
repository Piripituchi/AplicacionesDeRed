package Laboratorio3;

/**
 * hilos_herencia
 */
public class hilos_herencia extends Thread {
    public hilos_herencia(String str) {
        super(str);
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i+" "+getName());
        }
        System.out.println("Termina thread "+getName());
    }

    public static void main(String[] args) {
        new hilos_herencia("Lalo").start();
        new hilos_herencia("sssssssss").start();
        System.out.println("Termina thread main");
    }
}