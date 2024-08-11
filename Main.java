
import IO.Configurador;
import IO.LectorDatos;
import clases.AlgoritmoEvolutivo;
import clases.EvolucionDiferencial;
import clases.Individuo;
import clases.Poblacion;
import clasesAuxiliares.GestionaLog;
import clasesAuxiliares.Tiempos;

import java.io.File;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        Configurador config = new Configurador(args[0]);
        Tiempos tiempos = new Tiempos();

        //FOR DE LOS ARCHIVOS
        for (int i = 0; i < config.getArchivos().size(); i++) {
            LectorDatos datos = new LectorDatos(config.getArchivos().get(i));

            //FOR SEMILLA
            for (int j = 0; j < config.getSemillas().size(); j++) {
                //FOR POBLACION
                for (int k = 0; k < config.getPoblacion().size(); k++) {
                    //FOR ELITE
                    for (int l = 0; l < config.getEsquemaEvolucionElite().size(); l++) {
                        //FOR ALGORITMO
                        for (int m = 0; m < config.getAlgoritmo().size(); m++) {
                            GestionaLog log = new GestionaLog(config);
                            Random rand = new Random(config.getSemillas().get(j));
                            //Inicializacion

                            //tiempos.comienza();
                            Poblacion poblacion = new Poblacion(config.getPoblacion().get(k), config.getEsquemaEvolucionElite().get(l), rand, config, datos);
                            //tiempos.acaba();
                            //System.out.println("Tiempo GreedyAlea: "+tiempos.getTotal());

                            tiempos.comienza();
                            switch (config.getAlgoritmo().get(m)){
                                case "Evolutivo":
                                    AlgoritmoEvolutivo.algoritmoEvolutivo(poblacion, rand, config,datos,config.getEsquemaEvolucionElite().get(l),tiempos,log);
                                    log.registraLog("Tiempo: "+tiempos.getTotal()+"\n");
                                    log.escribeFichero(config.getRutaLog()+ File.separator+config.getArchivos().get(i)+"_"+config.getAlgoritmo().get(m)+"_"+config.getSemillas().get(j)+"_"+config.getPoblacion().get(k)+"_"+config.getEsquemaEvolucionElite().get(l)+".txt");
                                    break;

                                case "EDA":
                                    EvolucionDiferencial.evolucionDiferencial(config,rand,Boolean.TRUE,poblacion,datos,tiempos,log,config.getEsquemaEvolucionElite().get(l));
                                    log.registraLog("Tiempo: "+tiempos.getTotal()+"\n");
                                    log.escribeFichero(config.getRutaLog()+ File.separator+config.getArchivos().get(i)+"_"+config.getAlgoritmo().get(m)+"_"+config.getSemillas().get(j)+"_"+config.getPoblacion().get(k)+"_"+config.getEsquemaEvolucionElite().get(l)+".txt");
                                    break;

                                case "EDB":

                                    EvolucionDiferencial.evolucionDiferencial(config,rand,Boolean.FALSE,poblacion,datos,tiempos,log,config.getEsquemaEvolucionElite().get(l));
                                    log.registraLog("Tiempo: "+tiempos.getTotal()+"\n");
                                    log.escribeFichero(config.getRutaLog()+ File.separator+config.getArchivos().get(i)+"_"+config.getAlgoritmo().get(m)+"_"+config.getSemillas().get(j)+"_"+config.getPoblacion().get(k)+"_"+config.getEsquemaEvolucionElite().get(l)+".txt");
                                    break;

                                default:
                                    try {
                                        throw new Exception("Error en el archivo de parametros, algoritmo no valido");
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                            }

                        }


                    }


                }

            }

        }

    }


}
