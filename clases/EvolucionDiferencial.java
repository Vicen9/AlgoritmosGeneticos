package clases;

import IO.Configurador;
import IO.LectorDatos;
import clasesAuxiliares.GestionaLog;
import clasesAuxiliares.MetodosAuxiliares;
import clasesAuxiliares.Tiempos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * @class EvolucionDiferencial
 * @brief Representa la clase de evolución diferencial
 */
public class EvolucionDiferencial {

    /**
     * Metodo inicial de la clase que tiene la estructura del algoritmo diferencial, además diferenciamos que algoritmo diferencial utilizar y calculamos los tiempos respectivos
     * @param config Objeto para obtener los datos del configurador
     * @param rand Generador de números aleatorios
     * @param aOb Booleano que indica si es EDA o EDB
     * @param poblacion Parametro que recibe un objeto que representa una población ya inicializada
     * @param datos Objeto para obtener las matrices de ciudades y sus distancias respectivas
     * @param tiempos Clase que calcula los tiempos de ejecución de un programa
     * @param log Objeto para la escritura en el log
     * @param nElites Numero entero que indica el numero de elités
     */
    public static void evolucionDiferencial(Configurador config, Random rand, Boolean aOb, Poblacion poblacion, LectorDatos datos, Tiempos tiempos, GestionaLog log,Integer nElites){

        Poblacion pT = poblacion;
        Poblacion qT;
        Individuo[] iPrima = new Individuo[pT.getPoblacion().length];

        tiempos.acaba();
        for (int i = 0; i < config.getCondicionParada().get(0) && (tiempos.getTotal() <=(config.getCondicionParada().get(1)) ); i++) {
            tiempos.acaba();
            pT.calculaElite();

            if (i%1000 == 0){
                log.escribeIteraciones(pT,i);
            }

            if (i == 0){
                log.escribeIndividuos(pT);
            } else if ((i+1)%25000 == 0) {
                log.escribeIndividuos(pT);
            }

            for (int j = 0; j < pT.getPoblacion().length; j++) {
                ArrayList<Individuo> seleccionados;

                if (aOb == Boolean.TRUE){
                    seleccionados = seleccionEDA(pT,rand,config,pT.getPoblacion()[j]);
                }else {
                    seleccionados = seleccionEDB(pT,rand,config,pT.getPoblacion()[j]);
                }

                Individuo hijo = recombinacionTernaria(seleccionados.get(0),seleccionados.get(1),seleccionados.get(2),seleccionados.get(3),config,rand);
                iPrima[j] = sacaMejor(pT.getPoblacion()[j],hijo, datos.getDistancias());
            }

            qT = new Poblacion(iPrima,nElites);
            qT.evaluacionPoblacion(datos);
            qT.calculaElite();

            pT = qT;
        }
        log.escribeIndividuos(pT);
        log.escribeIteraciones(pT,Integer.MAX_VALUE);

    }

    /**
     * Selecciona k individuos aleatorios y devolvemos el individuo con mayor fitness
     * @param poblacion Parametro que recibe un objeto que representa una población ya inicializada
     * @param kDiferencial Numero entero que indica los participantes del torneo
     * @param rand Generador de números aleatorios
     * @return Devuelve el individuo ganador del torneo
     */
    private static Individuo torneo(Poblacion poblacion, Integer kDiferencial,Random rand){
        ArrayList<Individuo> participantes = new ArrayList<>();
        Individuo ganador = new Individuo();
        Integer alea;
        Integer aleamejor=Integer.MAX_VALUE;
        Integer iteKbest = kDiferencial;


        for (int i = 0; i < iteKbest; i++) {
            alea = rand.nextInt(0,poblacion.getPoblacion().length);
            participantes.add(poblacion.getPoblacion()[alea]);

            if (participantes.get(i).getFitness() < ganador.getFitness()){
                aleamejor = alea;
                ganador = participantes.get(i);
            }
        }
        ganador = new Individuo(ganador.getSolucion());
        return ganador;
    }

    /**
     * Intecambio de dos posiciones consecutivas y devolucion de las posiciones
     * @param padre Individuo seleccionado
     * @param rand Generador de números aleatorios
     * @return Devuelve las posiciones de intercambio del individuo padre
     */
    private static ArrayList<Integer> swap(Individuo padre, Random rand){
        ArrayList<Integer> salida = new ArrayList<>();
        Integer pos = rand.nextInt(0,padre.getSolucion().size()-1);
        Collections.swap(padre.getSolucion(),pos,pos+1);
        salida.add(padre.getSolucion().get(pos));
        salida.add(padre.getSolucion().get(pos+1));
        return salida;
    }

    /**
     * Devuelve el indivio que tiene mayor fitness
     * @param padre Individuo seleccionado
     * @param i2 Individuo 2 seleccionado
     * @param mDistancias Matriz de distancia
     * @return Devuelve individuo
     */
    private static Individuo sacaMejor(Individuo padre, Individuo i2,double[][] mDistancias){
        if (padre.getFitness() < MetodosAuxiliares.calculaFitness(i2.getSolucion(), mDistancias)){
            return padre;
        }
        return i2;
    }

    /**
     * Metodo de recombinacion ternaria (Swap al padre, intercambio cruzado de padre con alpha1, intercambio cruzado de alpha2 e individuo generado y OX2)
     * @param padre Individuo padre seleccionado
     * @param alpha1 Individuo alpha1 seleccionado
     * @param alpha2 Indivio alpha2 seleccionado
     * @param objetivo Indivio objetivo seleccionado
     * @param config Objeto para obtener los datos del configurador
     * @param rand Generador de números aleatorios
     * @return Devuelve un individuo
     */
    private static Individuo recombinacionTernaria(Individuo padre,Individuo alpha1, Individuo alpha2,Individuo objetivo,Configurador config, Random rand){

        //Paso 1 - swap al padre
        ArrayList<Integer> aleatorios = swap(padre, rand);

        //Paso 2 - intercambio cruzado de padre con alpha1
        Individuo nuevoIndividuo = new Individuo(alpha1.getSolucion());
        Integer posAleaAlpha1 = alpha1.getSolucion().indexOf(aleatorios.get(0));
        Integer pos2AleaAlpha1 = alpha1.getSolucion().indexOf(padre.getSolucion().get(posAleaAlpha1));
        Collections.swap(nuevoIndividuo.getSolucion(),posAleaAlpha1,pos2AleaAlpha1);

        //Paso 3 Intercambio cruzado con alpha2 y nuevo Individuo
        Integer posAleaAlpha2 = alpha2.getSolucion().indexOf(aleatorios.get(1));
        Integer pos2AleaNuevo = nuevoIndividuo.getSolucion().indexOf(aleatorios.get(1));
        Collections.swap(nuevoIndividuo.getSolucion(),posAleaAlpha2,pos2AleaNuevo);

        //Paso 4 OX2 de objetivo  y nuevo individuo
        return MetodosAuxiliares.cruceOX2(nuevoIndividuo, objetivo, rand, config);
    }

    /**
     * Selección de cuatros individuos(distintos), alpha1 y alpha2 seleccion aleatoria, objetivo seleccion por torneo
     * @param pT Parametro que recibe un objeto que representa la poblacion pT
     * @param rand Generador de números aleatorios
     * @param config Objeto para obtener los datos del configurador
     * @param individuo Seleccion de un individio secuencial
     * @return Devuelve una lista de cuatros individuos seleccionados
     */
    private static ArrayList<Individuo> seleccionEDA(Poblacion pT,Random rand, Configurador config, Individuo individuo){
        ArrayList<Individuo> salida = new ArrayList<>(pT.getPoblacion().length);
        Individuo alpha1;
        Individuo alpha2;
        Individuo padre= individuo;
        Individuo objetivo;

        boolean todosDiferentes;

        do {
            alpha1 = pT.getPoblacion()[rand.nextInt(0, pT.getPoblacion().length)];
            alpha2 = pT.getPoblacion()[rand.nextInt(0, pT.getPoblacion().length)];
            objetivo = torneo(pT, config.getkDiferencial(), rand);

            // Verificar que todos los individuos sean diferentes
            todosDiferentes = padre == alpha1 || padre == alpha2 || padre == objetivo || alpha2 == alpha1 || alpha2 == objetivo || alpha1 == objetivo;
        } while (todosDiferentes);

        padre= new Individuo(individuo.getSolucion());

        salida.add(padre);
        salida.add(alpha1);
        salida.add(alpha2);
        salida.add(objetivo);

        return salida;
    }

    /**
     * Selección de cuatros individuos(distintos), alpha1, alpha2 y objetivo seleccion por torneo
     * @param pT Parametro que recibe un objeto que representa la poblacion pT
     * @param rand Generador de números aleatorios
     * @param config Objeto para obtener los datos del configurador
     * @param individuo Seleccion de un individio secuencial
     * @return Devuelve una lista de cuatros individuos seleccionados
     */
    private static ArrayList<Individuo> seleccionEDB(Poblacion pT,Random rand, Configurador config, Individuo individuo){
        ArrayList<Individuo> salida = new ArrayList<>(pT.getPoblacion().length);
        Individuo alpha1;
        Individuo alpha2;
        Individuo padre=individuo;
        Individuo objetivo;
        boolean todosDiferentes;

        do {
            alpha1 = torneo(pT, config.getkDiferencial(), rand );
            alpha2 = torneo(pT, config.getkDiferencial(), rand );
            objetivo = torneo(pT, config.getkDiferencial(), rand );

            todosDiferentes = padre == alpha1 || padre == alpha2 || padre == objetivo || alpha2 == alpha1 || alpha2 == objetivo || alpha1 == objetivo;
        } while(todosDiferentes);

        padre= new Individuo(individuo.getSolucion());
        salida.add(padre);
        salida.add(alpha1);
        salida.add(alpha2);
        salida.add(objetivo);

        return salida;
    }



}
