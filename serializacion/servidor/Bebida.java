import java.io.Serializable;
public class Bebida implements Serializable {
    private String nombre;
    private double capacidad;
    private transient float porcentaje_alcohol;
    private transient boolean con_dosificador;
    private String tipo;
    private transient double lote;

    public Bebida(String n, double c, float f, boolean cd, String t, double l){
        nombre=n;
        capacidad=c;
        porcentaje_alcohol=f;
        con_dosificador=cd;
        tipo=t;
        lote=l;
    }

    public String getnombre() {
        return nombre;
    }

    public double getcapacidad() {
        return capacidad;
    }

    public float getp_alcohol() {
        return porcentaje_alcohol;
    }

    public boolean getc_dosificador() {
        return con_dosificador;
    }

    public String gettipo() {
        return tipo;
    }

    public double getlote() {
        return lote;
    }

}
