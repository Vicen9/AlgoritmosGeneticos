package clases;

import java.util.*;

/**
 * @class Individuo
 * @brief Representa la clase individuo
 */
public class Individuo {

    /**Lista de enteros que representa una solucion*/
    private final ArrayList<Integer> solucion;

    /**Coste asociado a la solucion*/
    private double fitness;

    /**Booleano que indica si el individuo a sido evaluado */
    private boolean evaluado;

    /**Booleano indica si los elite/s del individuo estan buscados  */
    private boolean elite;

    /**
     * Constructor parametrizado, la solucion se inicializa con el valor pasado por parametro
     * @param solucion  Lista de enteros que representa una solucion
     */
    public Individuo(ArrayList<Integer> solucion) {
        this.solucion = new ArrayList<>(solucion);
        this.fitness = 0;
        this.evaluado = false;
        this.elite=false;

    }

    /**
     * Constructor por defecto
     */
    public Individuo() {
        this.solucion = null;
        this.fitness = Double.MAX_VALUE;
        this.evaluado = false;
        this.elite=false;
    }

    /**
     * Método Getter de la variable solucion
     * @return solucion
     */
    public ArrayList<Integer> getSolucion() {
        return solucion;
    }

    /**
     * Método Getter de la variable fitness
     * @return fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Método Setter de la variable fitness
     * @param fitness
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Método Getter de la variable evaluado
     * @return evaluado
     */
    public boolean isEvaluado() {
        return evaluado;
    }

    /**
     * Método Setter de la variable evaluado
     * @param evaluado
     */
    public void setEvaluado(boolean evaluado) {
        this.evaluado = evaluado;
    }

    /**
     * Método Getter de la variable elite
     * @return elite
     */
    public boolean isElite() {
        return elite;
    }

    /**
     * Método Setter de la variable elite
     * @param elite
     */
    public void setElite(boolean elite) {
        this.elite = elite;
    }

    /**
     * Método de intercambio de todas las posiciones de un rango indicado
     * @param inicio Posicion inicial
     * @param fin Posicion final
     */
    public void swap(Integer inicio, Integer fin) {

        if (inicio >= 0 && fin < solucion.size()) {
            while (inicio < fin) {
                int temp = solucion.get(inicio);
                solucion.set(inicio,solucion.get(fin));
                solucion.set(fin,temp);
                inicio++;
                fin--;
            }

        }
    }

    /**
     * Método que indica si dos individuos son iguales o diferentes
     * @param indi Objeto individuo seleccionado
     * @return Devuelve True si son iguales y False si son distintos
     */
    public boolean iguales(Individuo indi) {
        if (this == indi) return true;
        if ( this.fitness == indi.getFitness() ){
            for (int i = 0; i < this.getSolucion().size(); i++) {
                if ( this.getSolucion().get(i) != indi.getSolucion().get(i)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
