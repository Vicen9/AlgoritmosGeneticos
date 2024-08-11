package clasesAuxiliares;

import IO.Configurador;
import IO.LectorDatos;
import clases.Individuo;
import clases.Poblacion;

import java.util.*;

/**
 * @class MetodosAuxiliares
 * @brief Representa la clase individuo
 */
public class MetodosAuxiliares {

    /**
     * Método que busca los elites de pt en qt, en caso de que no estén llamamos al método "reemplazaEliNoEncontrado"
     * @param pt Parametro que recibe un objeto que representa la poblacion pT
     * @param qt Parametro que recibe un objeto que representa la poblacion qT
     * @param config Objeto para obtener los datos del configurador
     * @param nElites Numero entero que indica el numero de elités
     * @param rand Generador de números aleatorios
     */
    public static void reemplazaElites(Poblacion pt, Poblacion qt, Configurador config, Integer nElites, Random rand){
        Boolean individuoEncontrado;
        //Nº de elites de pt en qt
        for (int i = 0; i < nElites; i++) {
            individuoEncontrado = false;
            //Recorre Qt
            for (int j = 0; j < qt.getPoblacion().length && !individuoEncontrado; j++) {

                individuoEncontrado = pt.getElites()[i].iguales(qt.getPoblacion()[j]);

            }

            if(individuoEncontrado == false){
                reemplazaEliNoEncontrado(pt.getElites()[i],qt,rand,config);
            }
        }
    }

    /**
     * Método que elimina un indivio de qt mediante un aleatorio de k peores y lo sustituye por el elite de pt
     * @param elitePt Individuo seleccionado que representa un elite de pt
     * @param qt Parametro que recibe un objeto que representa la poblacion qT
     * @param rand Generador de números aleatorios
     * @param config Objeto para obtener los datos del configurador
     */
    public static void reemplazaEliNoEncontrado(Individuo elitePt, Poblacion qt, Random rand, Configurador config){
        ArrayList<Map.Entry<Double,Integer>> vectorOrdenado = new ArrayList<>();

        for (int i = 0; i < qt.getPoblacion().length; i++) {
            vectorOrdenado.add(new AbstractMap.SimpleEntry<>(qt.getPoblacion()[i].getFitness(),i));
        }

        Collections.sort(vectorOrdenado, new Comparator<Map.Entry<Double, Integer>>() {
            @Override
            public int compare(Map.Entry<Double, Integer> o1, Map.Entry<Double, Integer> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });

        qt.getPoblacion()[vectorOrdenado.get(rand.nextInt(0,config.getkWorst()) ).getValue()] = elitePt;
    }


    /**
     * @brief Realiza el cruce OX2 entre dos individuos para generar un nuevo individuo.
     *
     * Este método implementa el cruce OX2 entre dos individuos para crear un nuevo individuo resultante del cruce.
     *
     * @param p1 Primer individuo para el cruce.
     * @param p2 Segundo individuo para el cruce.
     * @param rand Objeto Random utilizado para generar valores aleatorios.
     * @param config Objeto Configurador con la configuración para el cruce.
     * @return Un nuevo objeto Individuo generado a partir del cruce OX2 entre p1 y p2.
     */
    public static Individuo cruceOX2(Individuo p1, Individuo p2, Random rand, Configurador config){
        Queue<Integer> aux = new LinkedList<>();
        ArrayList<Integer> vectorNew = new ArrayList<>();
        ArrayList<Integer> seleccionados = new ArrayList<>();


        for (int i = 0; i < p1.getSolucion().size(); i++) {
            if (rand.nextInt(0,101) < config.getPorcentajeOX2()){
                aux.add(p1.getSolucion().get(i));
                seleccionados.add(p1.getSolucion().get(i));
            }
        }

        for (int i = 0; i < p1.getSolucion().size(); i++) {
            if ( seleccionados.contains(p2.getSolucion().get(i))){
                vectorNew.add(aux.poll());
            }else {
                vectorNew.add(p2.getSolucion().get(i));
            }
        }
        return new Individuo(vectorNew);
    }


    /**
     * @brief Calcula el valor de fitness para una solución o individuo.
     *
     * Este método calcula el valor de fitness para la solución de un individuo.
     *
     * @param sol Lista de enteros que representa una solución.
     * @param mDistancias Matriz de distancias entre ubicaciones.
     * @return El valor de fitness calculado para la solución proporcionada.
     */
    public static double calculaFitness(ArrayList<Integer> sol, double[][] mDistancias){
        double fitness = 0.0;
        for (int i = 0; i < sol.size()-1; i++) {
            fitness+= mDistancias[sol.get(i)][sol.get(i+1)];
        }
        return fitness;
    }


    /**
     * @brief Genera una solución utilizando el algoritmo Greedy Aleatorizado.
     *
     * Este método implementa el algoritmo Greedy Aleatorizado para generar una solución basada en la matriz de distancias.
     *
     * @param datos Objeto LectorDatos que contiene información de distancias entre ubicaciones.
     * @param numGreedy Número máximo de candidatos considerados en cada iteración del algoritmo.
     * @param rand Objeto Random utilizado para la generación de valores aleatorios.
     * @return Una lista de enteros que representa la solución generada por el algoritmo Greedy Aleatorizado.
     */
    public static ArrayList<Integer> greedyAleatorizado(LectorDatos datos, Integer numGreedy, Random rand){

        PriorityQueue<Map.Entry<Integer, Double>> lista = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        Integer tam = datos.getDistancias()[0].length;
        Set<Integer> solSet = new HashSet<>(tam); // Utilizamos un HashSet para comprobar la pertenencia en O(1)
        Integer ini = rand.nextInt(0, tam);
        ArrayList<Integer> solSalida = new ArrayList<>(tam);

        for (int i = 0; i < tam; i++) {
            lista = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
            for (int j = 0; j < tam; j++) {
                if (ini != j && !solSet.contains(j)) {
                    lista.add(new AbstractMap.SimpleEntry<>(j, datos.getDistancias()[i][j]));
                }
            }

            // Seleccionar aleatoriamente un candidato
            int numToSelect = Math.min(numGreedy, lista.size());
            int selectedIdx = rand.nextInt(numToSelect);
            Integer kRandom = null;


            Iterator<Map.Entry<Integer, Double>> iterator = lista.iterator();
            for (int idx = 0; idx <= selectedIdx; idx++) {
                Map.Entry<Integer, Double> entry = iterator.next();
                if (idx == selectedIdx) {
                    kRandom = entry.getKey();
                }
            }

            // Agregar a la solución y actualizar el conjunto de soluciones
            solSalida.add(kRandom);
            solSet.add(kRandom);

            // Actualizar el valor de 'ini' para la siguiente iteración
            ini = kRandom;
        }

        return solSalida;

    }


    /**
     * @brief Genera una solución aleatoria para inicializar la población.
     *
     * Este método genera una solución aleatoria para inicializar la población basada en la matriz de distancias proporcionada.
     *
     * @param datos Objeto LectorDatos que contiene información de distancias entre ubicaciones.
     * @param rand Objeto Random utilizado para la generación de valores aleatorios.
     * @return Una lista de enteros que representa una solución aleatoria para inicializar la población.
     */
    public static ArrayList<Integer> inicializacionAleatoria(LectorDatos datos, Random rand){
        Integer tam = datos.getDistancias()[0].length;
        ArrayList<Integer> solSalida = new ArrayList<>(tam);
        ArrayList<Integer> vectorTemp = new ArrayList<>(tam);

        for (int i = 0; i < tam; i++) {
            vectorTemp.add(i);
        }
        Integer kRandom;
        for (int i = 0; i < tam; i++) {
            kRandom = rand.nextInt(0,vectorTemp.size());
            solSalida.add(vectorTemp.get(kRandom));
            vectorTemp.remove(vectorTemp.get(kRandom));
        }
        return  solSalida;
    }


    /**
     * @brief Dada una población muestra sus fitness por pantalla.
     *
     * @param poblacion Poblacion que se va a mostrar por pantalla.
     */
    public static void muestraIndiPoblacion(Poblacion poblacion){
        for (int i = 0; i < poblacion.getPoblacion().length; i++) {
            System.out.println(i+": "+poblacion.getPoblacion()[i].getSolucion()+ ", Fitness: "+poblacion.getPoblacion()[i].getFitness()+" "+poblacion.getPoblacion()[i].isElite());
        }
    }

}