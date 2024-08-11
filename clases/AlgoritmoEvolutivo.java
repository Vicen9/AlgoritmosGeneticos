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
 * @class AlgoritmoEvolutivo
 * @brief Representa la clase del algoritmo evolutivo
 */
public class AlgoritmoEvolutivo {


    /**
     * Método principal de la clase AlgoritmoEvolutivo, dado una población pT, primero se seleccionan los individuos
     * a recombinar, posteriormente se realiza el cruce y la mutación de dichos individuos, se crea una nueva población
     * con Pprima y esta se evalúa y se calculan los élites, reemplazamos los élites de la población anterior y sustituimos
     * pT+1 por qT
     *
     * @param poblacion Parametro en el que se recibe una población.
     * @param rand Variable de la clase random.
     * @param config  Objeto de la clase configurador para obtener datos.
     * @param datos Objeto de la clase LectorDatos que recoge los datos dado un fichero
     * @param nElites Numero de élites que van a haber en la población
     * @param tiempos Objeto de la clase tiempos usada para medir y calcular cuanto tarda el algoritmo
     * @param log Objeto de la clase log, encargado de escribir el log
     */
    public static void algoritmoEvolutivo(Poblacion poblacion, Random rand, Configurador config, LectorDatos datos , Integer nElites, Tiempos tiempos, GestionaLog log){
        //Inicializacion + Evaluacion
        Poblacion pT = poblacion;

        tiempos.acaba();
        for (int i = 0; i < config.getCondicionParada().get(0) && (tiempos.getTotal() <=(config.getCondicionParada().get(1)) ); i++) {
            tiempos.acaba();
            //Calcula los elite de Pt 
            pT.calculaElite();

            //Escribe el fichero log en cada 1000 iteraciones
            if (i%1000 == 0){
                log.escribeIteraciones(pT,i);
            }

            if (i == 0){
                log.escribeIndividuos(pT);
            } else if ((i+1)%25000 == 0) {
                log.escribeIndividuos(pT);
            }


            //Seleccion
            Individuo[] IPPrima = AlgoritmoEvolutivo.seleccion(rand,pT,config);
            //Cruce + mutacion
            IPPrima = AlgoritmoEvolutivo.recombinacion(pT,IPPrima,rand,config);
            //Meter P`t en Qt
            Poblacion qT = new Poblacion(IPPrima, nElites);
            //Evaluamos Qt
            qT.evaluacionPoblacion(datos);
            qT.calculaElite();
            //Reemplazamos elites
            MetodosAuxiliares.reemplazaElites(pT,qT,config,nElites,rand);
            //Reemplazar Pt+1 con Qt
            pT = qT;

        }


        log.escribeIndividuos(pT);
        log.escribeIteraciones(pT,Integer.MAX_VALUE);
    }


    /**
     * Método que representa a la función de selección, dada una población, selecciona quien se va a cruzar y pasar a la siguiente generación
     * esto se hace mediante un torneo de kbest, que sacará el mejor dentro de los kbest seleccionados aleatoriamente.
     *
     * @param rand Variable de la clase random.
     * @param poblacion Parametro en el que se recibe una población.
     * @param config Objeto de la clase configurador para obtener datos.
     * @return Devuelve un vector de individuos que representa una población seleccionada
     */
    public static Individuo[] seleccion(Random rand,Poblacion poblacion ,Configurador config){
        Individuo[] pPrima = new Individuo[poblacion.getPoblacion().length];
        ArrayList<Individuo> sacar = new ArrayList<>();

        for (int i = 0; i < poblacion.getPoblacion().length; i++) {
            sacar.add(poblacion.getPoblacion()[i]);
        }

        for (int i = 0; i < poblacion.getPoblacion().length; i++) {
            pPrima[i] = torneo(config.getkBest(),poblacion,sacar,rand);
        }

        return pPrima;
    }


    /**
     * Método que realiza el torneo, este se realiza escogiendo kBest individuos aleatorios de una poblacion,
     * y de estos individuos, se devuelve el mejor.
     *
     * @param kBest Número de participantes del torneo.
     * @param poblacion Parametro en el que se recibe una población.
     * @param sacar Vector usado para no repetir padres en un torneo.
     * @param rand Variable de la clase random.
     * @return Devuelve un individuo, que es el ganador del torneo.
     */
    public static Individuo torneo(Integer kBest,Poblacion poblacion ,ArrayList<Individuo> sacar , Random rand){
        ArrayList<Individuo> participantes = new ArrayList<>();
        Individuo ganador = new Individuo();
        Integer alea;
        Integer aleamejor=Integer.MAX_VALUE;
        Integer iteKbest = kBest;

        if (sacar.size() < iteKbest ){
            iteKbest = sacar.size();
        }

        for (int i = 0; i < iteKbest; i++) {
            alea = rand.nextInt(0,sacar.size());
            participantes.add(poblacion.getPoblacion()[alea]);

            if (participantes.get(i).getFitness() < ganador.getFitness()){
                aleamejor = alea;
                ganador = participantes.get(i);
            }
        }
        sacar.remove(aleamejor);
        return ganador;
    }


    /**
     *El método recombinación, lo que hace es que dado dos padres, saca 2 hijos,
     * que son una combinación entre ambos mediante OX2, posteriormente dependiendo del porcentaje
     * de mutación mutarán o no.
     *
     * @param poblacion Parámetro en el que se recibe una población.
     * @param pPr Vector de individuos Pprima, que son los seleccionados.
     * @param rand Variable de la clase random.
     * @param config Objeto de la clase configurador para obtener datos.
     * @return Devuelve un vector de individuos ya recombinados y mutados, es decir Pprima final.
     */
    public static Individuo[] recombinacion(Poblacion poblacion, Individuo[] pPr,Random rand, Configurador config){
        Individuo[] pPrima = new Individuo[poblacion.getPoblacion().length];
        Individuo hijo1;
        Individuo hijo2;

        for (int i = 0; i < poblacion.getPoblacion().length; i+=2) {
            //Realizalizamos la cruce
            if (rand.nextInt(0,101) < config.getPorcentajeCruce()){
                hijo1 = MetodosAuxiliares.cruceOX2(pPr[i],pPr[i+1],rand,config);
                hijo2 = MetodosAuxiliares.cruceOX2(pPr[i+1],pPr[i],rand,config);
            }else {
                hijo1 = pPr[i];
                hijo2 = pPr[i+1];
            }

            //Mutacion
            if (rand.nextInt(0,101) < config.getPorcentajeMutacion()){
                mutacion(hijo1,rand);
                mutacion(hijo2,rand);
            }

            pPrima[i] = hijo1;
            pPrima[i+1] = hijo2;
        }

        return pPrima;
    }

    /**
     * Modifica un Individuo realizandole un swap entre 2 de los valores de su vector solución
     * @param h Individuo hijo
     * @param rand Objeto de la clase random.
     */
    public static void mutacion(Individuo h,Random rand){
        Integer r1;
        Integer r2;
        do {
            r1 = rand.nextInt(0,h.getSolucion().size());
            r2 = rand.nextInt(0,h.getSolucion().size());
        }while(r1 != r2);

        Collections.swap(h.getSolucion(),r1,r2);
    }

}
