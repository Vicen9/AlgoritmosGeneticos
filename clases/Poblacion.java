package clases;

import IO.Configurador;
import IO.LectorDatos;
import clasesAuxiliares.MetodosAuxiliares;

import java.util.*;

/**
 * @class Poblacion
 * @brief Representa la clase poblacion
 */
public class Poblacion {

    //Vector de individuos que representa una población
    private Individuo[] poblacion;

    //Vector de individuos que son Élite en una población
    private Individuo[] elites;


    /**
     * @brief Constructor de la clase Poblacion.
     *
     * Este constructor inicializa una población con un número dado de individuos y elites.
     *
     * @param nIndi Número de individuos en la población.
     * @param nElites Número de individuos élite en la población.
     * @param rand Objeto de tipo Random utilizado para inicializar la población.
     * @param config Objeto de tipo Configurador con la configuración para la inicialización.
     * @param datos Objeto de tipo LectorDatos para obtener datos iniciales.
     */
    public Poblacion(Integer nIndi, Integer nElites, Random rand, Configurador config, LectorDatos datos) {
        poblacion = new Individuo[nIndi];
        elites = new Individuo[nElites];
        inicializaPoblacion(rand,config,datos);
    }

    /**
     * @brief Constructor de la clase Poblacion que recibe una población existente y el número de élites.
     *
     * Este constructor realiza una copia de los individuos de la población proporcionada y calcula los individuos élite.
     *
     * @param poblacion Arreglo de objetos Individuo que representa la población inicial.
     * @param nElites Número de individuos élite en la población.
     */
    public Poblacion(Individuo[] poblacion, Integer nElites) {
        this.poblacion = new Individuo[poblacion.length];
        for (int i = 0; i < poblacion.length; i++) {
            this.poblacion[i] = new Individuo(poblacion[i].getSolucion()); // Copia de cada elemento
        }
        elites = new Individuo[nElites];
        calculaElite();
    }


    /**
     * @brief Inicializa la población de individuos utilizando diferentes estrategias basadas en la configuración.
     *
     * Este método crea la población de individuos aplicando estrategias específicas según la configuración proporcionada.
     *
     * @param rand Objeto Random utilizado para generar valores aleatorios.
     * @param config Objeto Configurador con la configuración para inicializar la población.
     * @param datos Objeto LectorDatos que contiene los datos necesarios para la inicialización.
     */
    private void inicializaPoblacion(Random rand, Configurador config, LectorDatos datos){
        for (int i = 0; i < poblacion.length; i++) {

            if ( rand.nextInt(0,101) < config.getPorcIndiAl()){

                this.poblacion[i] = new Individuo(MetodosAuxiliares.inicializacionAleatoria(datos,rand));
            }else {

                this.poblacion[i] = new Individuo(MetodosAuxiliares.greedyAleatorizado(datos,config.getGreedyAlea(),rand));

            }
            evaluacion(this.poblacion[i], datos);
        }
    }

    /**
     * @brief Calcula y establece los individuos élite dentro de la población.
     *
     * Este método identifica y marca los individuos élite de la población en función de su valor de fitness.
     * Los individuos élite se almacenan en el vector de élites.
     */
    public void calculaElite(){
        //Vector de pair
        ArrayList<Map.Entry<Double,Individuo>> listaTotal = new ArrayList<>();

        for (int i = 0; i < this.poblacion.length; i++) {
            this.poblacion[i].setElite(false);
            listaTotal.add(new AbstractMap.SimpleEntry<>(this.poblacion[i].getFitness(),this.poblacion[i]));
        }

        Collections.sort(listaTotal, new Comparator<Map.Entry<Double, Individuo>>() {
            @Override
            public int compare(Map.Entry<Double, Individuo> o1, Map.Entry<Double, Individuo> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < this.elites.length; i++) {
            this.elites[i] = listaTotal.get(i).getValue();
            this.elites[i].setElite(true);
        }
    }

    /**
     * @brief Realiza la evaluación de un individuo si no ha sido evaluado previamente.
     *
     * Este método calcula el fitness de un individuo utilizando los datos proporcionados y lo marca como evaluado.
     *
     * @param indi Objeto Individuo que se evaluará.
     * @param datos Objeto LectorDatos que contiene información necesaria para la evaluación.
     */
    private void evaluacion(Individuo indi,LectorDatos datos){
        if (indi.isEvaluado() == false){
            indi.setFitness( MetodosAuxiliares.calculaFitness(indi.getSolucion(), datos.getDistancias()));
            indi.setEvaluado(true);
        }
    }


    /**
     * @brief Realiza la evaluación de todos los individuos en la población si no han sido evaluados previamente.
     *
     * Este método itera sobre cada individuo en la población y realiza su evaluación si aún no ha sido evaluado.
     *
     * @param datos Objeto LectorDatos que contiene la información necesaria para la evaluación.
     */
    public void evaluacionPoblacion(LectorDatos datos){
        for (int i = 0; i < this.poblacion.length; i++) {
            evaluacion(this.poblacion[i],datos);
        }
    }

    /**GETTER Y SETTER*/
    public Individuo[] getPoblacion() {
        return poblacion;
    }

    public Individuo[] getElites() {
        return elites;
    }

}
