package Laboratorio3.Eje2;

import java.util.Random;
import java.util.Vector;

public class SumaVector extends Thread {
    private String nombre;
    private Vector<Integer> vector;
    private int tam;

    public SumaVector(String n){
        nombre=n;
    }

    public void run() {
        int suma=0;
        int suma_2=0;
        double promedio;
        int aux;
        Random rnd = new Random();
        this.tam=rnd.nextInt(10);
        this.vector=new Vector<Integer>(this.tam);
        System.out.println("Iniciado Thread: "+this.nombre+" Con tamaño de vector: "+Integer.toString(this.tam));
        for (int i = 0; i < this.tam; i++) {
            aux=rnd.nextInt(100);
            System.out.println("Añadiendo al Thread "+this.nombre+": "+Integer.toString(aux));
            this.vector.add(aux);
        }
        for (int i = 0; i < this.tam; i++) {
            suma=suma+this.vector.elementAt(i);
            suma_2=suma_2+(this.vector.elementAt(i)*this.vector.elementAt(i));
        }
        promedio=(double)suma/this.tam;
        System.out.println("Thread: "+this.nombre+" Suma: "+Integer.toString(suma));
        System.out.println("Thread: "+this.nombre+" Suma de cuadrados : "+Integer.toString(suma_2));
        System.out.println("Thread: "+this.nombre+" Promedio: "+Double.toString(promedio));
        System.out.println("Termina Thread: "+this.nombre);
    }

    public static void main(String[] args) {
        SumaVector v1 = new SumaVector("Vector 1");
        SumaVector v2 = new SumaVector("Vector 2");

        v1.start();
        v2.start();
    }


}
