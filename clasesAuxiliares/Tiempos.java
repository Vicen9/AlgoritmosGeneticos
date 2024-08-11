package clasesAuxiliares;

public class Tiempos {
    private double inicio;
    private double fin;

    private double total;

    public Tiempos() {
    }

    public void comienza(){
        inicio = System.currentTimeMillis();
    }

    public void acaba(){
        fin = System.currentTimeMillis();

        total = (fin-inicio)/1000.00;


    }

    public double getTotal() {
        return total;
    }
}
